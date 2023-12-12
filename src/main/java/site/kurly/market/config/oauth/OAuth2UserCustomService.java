package site.kurly.market.config.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Service;
import site.kurly.market.domain.Member;
import site.kurly.market.repository.MemberRepository;

import java.util.Map;

// OAuth2 로그인 시 사용자 정보를 가져오는 데 사용함
// 굳이 DefaultOAuth2UserService를 상속받아 커스텀한 이유는 OAuth2 로그인으로 얻은 사용자 정보를, 우리의 데이터베이스에 유지하거나 업데이트하는 로직을 추가하기 위함임
@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy(); // Spring Security의 기본 RedirectStrategy 사용

    // 요청을 바탕으로 유저 정보를 담은 객체 반환
    // OAuth2User는 OAuth 2.0 프로토콜을 사용하여 인증된 사용자의 정보를 나타내는 인터페이스이다.
    // 이 인터페이스는 주로 Spring Security와 함께 사용되며, OAuth 2.0 기반의 인증 프로바이더(구글, 깃허브 등)로부터 받은 사용자 정보를 표현한다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        save(user); // 추가 컬럼을 받기 위해 이동
        return user; // OAuth2User 반환
    }

    // 유저가 있으면 업데이트, 없으면 유저 생성 (이미 가입한 유저 및 가입하지 않은 유저 모두 처리하기 위함?)
    // 여기서는 생성만 하도록 함
    private Member save(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String nickname = (String) attributes.get("profile_nickname");
        String email = (String) attributes.get("account_email");
        String auth = (String) oAuth2User.getAttribute("id");

        Member member = memberRepository.findByEmail(email)
                .orElse(Member.builder()
                        .nickname(nickname)
                        .email(email)
                        .auth(auth)
                        .build());

        return memberRepository.save(member);
    }
}











