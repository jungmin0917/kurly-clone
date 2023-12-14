package site.kurly.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import site.kurly.market.service.MemberService;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 로그인 페이지로 이동
    @GetMapping("/login")
    public String loginForm(){
        return "member/loginForm";
    }

    // 회원가입 페이지로 이동
    @GetMapping("/join")
    public String joinForm(){
        return "member/joinForm";
    }

    // 회원가입 - 추가정보 입력 페이지로 이동
    @GetMapping("/joinAdd")
    public String joinAddForm(){
        return "member/joinAddInfoForm";
    }
}
