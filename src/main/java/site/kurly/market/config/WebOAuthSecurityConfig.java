package site.kurly.market.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import site.kurly.market.config.jwt.TokenProvider;
import site.kurly.market.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import site.kurly.market.config.oauth.OAuth2SuccessHandler;
import site.kurly.market.config.oauth.OAuth2UserCustomService;
import site.kurly.market.filter.TokenAuthenticationFilter;
import site.kurly.market.repository.RefreshTokenRepository;
import site.kurly.market.service.MemberService;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

// 스프링 시큐리티 설정을 담은 클래스. OAuth2 로그인 및 토큰 기반의 인증 설정, 필터 등을 구성한다

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;

    @Bean
    public WebSecurityCustomizer configure() {
        // 특정 요청 및 특정 리소스에 스프링 시큐리티 기능 비활성화
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/img/**", "/css/**", "/js/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 이 역시 책에서 한 방식은 deprecated 됐으므로 람다 DSL을 이용한 방식을 사용하도록 하겠다.
        return http
                // 토큰 방식으로 인증을 하기 때문에 기존에 사용하던 폼로그인, 세션 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 헤더를 확인할 커스텀 필터 추가
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 토큰 재발급 URL은 인증 없이 접근 가능하도록 설정. 나머지 API URL은 인증 필요
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/api/token").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2Login(o -> o
                        .loginPage("/login") // OAuth2 로그인이 필요한 인증 페이지의 경우 이 페이지로 리디렉션 시킴
                        // OAuth2 로그인 중 발생하는 Authorization 요청과 관련된 상태를 저장하기 위한 AuthorizationRequestRepository를 설정함.
                        .authorizationEndpoint(authorization -> authorization
                                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                        )
                        .successHandler(oAuth2SuccessHandler()) // 인증 성공 시 실행할 커스텀 핸들러를 반환하는 메서드를 지정함
                        .failureHandler((request, response, exception) -> {
                            request.getSession().setAttribute("error.message", exception.getMessage());
                        })
                        .userInfoEndpoint(endpoint -> endpoint // customUserService 설정 (OAuth2 로그인의 로직을 담당하는 Service 설정)
                                .userService(oAuth2UserCustomService) // 여기서 DB에 User를 저장함
                        )
                )
                .logout(l -> l
                        .logoutSuccessUrl("/login")
                )
                .build();
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider, refreshTokenRepository, oAuth2AuthorizationRequestBasedOnCookieRepository(), memberService);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}













