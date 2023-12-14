package site.kurly.market.interceptor;

// 회원가입 시 추가 정보를 입력하지 않았을 때 강제로 리다이렉트시키기 위한 인터셉터

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import site.kurly.market.domain.Member;
import site.kurly.market.service.MemberService;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberInterceptor implements HandlerInterceptor {
    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 현재 인증정보를 시큐리티 컨텍스트 홀더로부터 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info(authentication.toString());

        // 로그인하여 인증된 상태라면
        if(authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")){

            log.info(authentication.toString());

            String email = authentication.getName();
            Member member = memberService.findByEmail(email);

            // 해당 회원이 존재하고, 특정 필드가 비어있을 경우
            if(member != null && member.getNickname() == null){
                response.sendRedirect("/joinAdd"); // 추가컬럼 받는 페이지로 이동
                return false; // 요청을 중단하고 기존 요청을 더 이상 진행하지 않도록 설정함
            }
        }

        return true;
    }
}
