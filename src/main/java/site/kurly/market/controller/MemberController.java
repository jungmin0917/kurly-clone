package site.kurly.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import site.kurly.market.service.MemberService;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 로그인 폼으로 이동
    @GetMapping("/login")
    public String loginForm(){
        return "member/loginForm";
    }
}
