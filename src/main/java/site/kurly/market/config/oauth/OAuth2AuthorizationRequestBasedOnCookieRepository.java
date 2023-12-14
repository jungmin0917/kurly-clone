package site.kurly.market.config.oauth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.util.WebUtils;
import site.kurly.market.util.CookieUtils;

public class OAuth2AuthorizationRequestBasedOnCookieRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"; // OAuth2 인증 요청 저장 시 사용할 쿠키의 이름을 정의하는 상수
    private final static int COOKIE_EXPIRE_SECONDS = 18000; // 쿠키 만료 시간 지정하는 상수

    // 저장된 OAuth2 인증 요청을 제거하는 메서드. 저장된 요청이 쿠키에 존재할 경우, 쿠키에해서 해당 정보를 삭제하고 인증 요청을 반환함.
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthorizationRequest authorizationRequest = this.loadAuthorizationRequest(request); // 저장된 인증 요청

        // 저장된 요청이 존재할 경우 쿠키에서 해당 정보를 삭제
        if (authorizationRequest != null) {
            removeAuthorizationRequestCookies(request, response);
        }

        return authorizationRequest;
    }

    // 저장된 OAuth2 인증 요청을 로드하는 메서드. 쿠키에서 인증 요청 정보를 읽어와 객체로 역직렬화한 후 반환함.
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);

        return CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class);
    }

    // OAuth2 인증 요청을 저장하는 메서드. 주어진 요청이 null인 경우 쿠키에서 해당 정보를 삭제하고, 아니면 인증 요청을 쿠키에 저장하고 설정된 만료 시간까지 유지함.
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) { // OAuth2 인증 요청이 null인 경우
            removeAuthorizationRequestCookies(request, response); // 쿠키에서 인증 요청 정보 삭제함
            return;
        }

        // 쿠키에 인증 요청 정보 저장함
        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    }
}






