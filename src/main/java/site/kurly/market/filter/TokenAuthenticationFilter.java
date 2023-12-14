package site.kurly.market.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import site.kurly.market.config.jwt.TokenProvider;

import java.io.IOException;

// 토큰을 이용한 인증을 처리하는 필터. 요청 헤더에서 토큰을 추출하고, 해당 토큰이 유효하면 인증 정보를 설정한다.

@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더의 Authorization 키의 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);

        // 가져온 값에서 접두사 제거
        String token = getAccessToken(authorizationHeader);

        // 가져온 토큰이 유효한지 확인하고, 유효한 때는 인증 정보 설정
        if (tokenProvider.validToken(token)) { // 토큰이 유효하다면
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보를 관리하는 시큐리티 컨텍스트에 인증 정보를 설정함.

            // 제대로 인증 정보 설정됐는지 확인함
            log.info(SecurityContextHolder.getContext().getAuthentication().toString());
        }

        // 절대절대 주의!!!!!!!!!!
        // 필터의 doFilterInternal 내부에서 필터 체인을 doFilter하지 않으면 다음으로 넘어가지 않아 하얀 화면만 뜬다!!!
        filterChain.doFilter(request, response);
    }

    // TOKEN_PREFIX를 잘라서 값을 반환함
    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}

