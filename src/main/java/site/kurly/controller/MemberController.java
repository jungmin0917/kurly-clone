package site.kurly.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import site.kurly.domain.Member;
import site.kurly.service.MemberService;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 로그인 페이지로 이동
    @GetMapping("/login")
    public String loginForm(Model model) {
        return "member/loginForm";
    }

    // 회원가입 페이지로 이동
    @GetMapping("/join")
    public String joinForm() {
        return "member/joinForm";
    }

    // 회원가입 - 추가정보 입력 페이지로 이동
    @GetMapping("/joinAddInfo")
    public String joinAddForm() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인하지 않았거나 익명 사용자인 경우
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }else{ // 로그인한 데다 회원인 경우
            Member member = memberService.findByEmail(((User)authentication.getPrincipal()).getUsername());

            // 이미 추가 컬럼을 입력한 경우
            if(member.getPassword() != null){
                return "redirect:/login";
            }
        }

        return "member/joinAddInfoForm";
    }

}
