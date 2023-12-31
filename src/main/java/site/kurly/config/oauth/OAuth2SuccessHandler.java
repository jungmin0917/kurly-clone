package site.kurly.config.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import site.kurly.config.jwt.TokenProvider;
import site.kurly.domain.Member;
import site.kurly.domain.RefreshToken;
import site.kurly.repository.RefreshTokenRepository;
import site.kurly.service.MemberService;
import site.kurly.util.CookieUtils;

import java.io.IOException;
import java.time.Duration;

// OAuth2 로그인 성공 시 처리를 담당하는 클래스. OAuth2 인증 성공 시 사용자 정보 가져오기, 리프레시 토큰 생성 및 저장, 액세스 토큰 생성 등을 수행한다.

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final String REDIRECT_PATH = "/"; // 메인으로 가게 함

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final MemberService memberService;

    // onAuthenticationSuccess가 실행되지 않는 것에는 핸들러 내에서 예외가 발생했을 가능성이 있다.
    // 예외가 아니고 메서드 시그니처를 다르게 적어서 onAuthenticationSuccess를 완전 다른 메서드로 작성한 거였음
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("oauth login success...");

        // 사용자 정보를 가져옴
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        ObjectMapper objectMapper = new ObjectMapper(); // JSON 형태의 값을 편하게 사용하기 위한 jackson databind 라이브러리 사용
        String jsonString = objectMapper.writeValueAsString(oAuth2User.getAttributes());
        JsonNode rootNode = objectMapper.readTree(jsonString);
        String email = rootNode.path("kakao_account").path("email").asText();
        Member member = memberService.findByEmail(email);

        // 리프레시 토큰 생성 -> 저장 -> 쿠키에 저장
        String refreshToken = tokenProvider.generateToken(member, REFRESH_TOKEN_DURATION); // 리프레시 토큰 생성
        saveRefreshToken(member.getNo(), refreshToken); // DB에 리프레시 토큰 저장
        addRefreshTokenToCookie(request, response, refreshToken); // 쿠키에 리프레시 토큰 저장

        // 액세스 토큰 생성 -> URL에 액세스 토큰 추가
        String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION); // 액세스 토큰 생성
        addAccessTokenToCookie(request, response, accessToken); // 쿠키에 액세스 토큰 저장

        // 인증 관련 설정값, 쿠키 제거
        clearAuthenticationAttributes(request, response);

        // 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, REDIRECT_PATH);
    }

    // 생성된 리프레시 토큰을 전달받아 데이터베이스에 저장
    private void saveRefreshToken(Long memberNo, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberNo(memberNo)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(memberNo, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    // 생성된 리프레시 토큰을 쿠키에 저장
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtils.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    // 생성된 액세스 토큰을 쿠키에 저장
    private void addAccessTokenToCookie(HttpServletRequest request, HttpServletResponse response, String accessToken) {
        int cookieMaxAge = (int) ACCESS_TOKEN_DURATION.toSeconds();
        CookieUtils.deleteCookie(request, response, ACCESS_TOKEN_COOKIE_NAME);
        CookieUtils.addCookie(response, ACCESS_TOKEN_COOKIE_NAME, accessToken, cookieMaxAge);
    }

    // 인증 관련 설정값, 쿠키 제거
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}













