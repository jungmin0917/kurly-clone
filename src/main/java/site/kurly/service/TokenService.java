package site.kurly.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.kurly.domain.Member;
import site.kurly.config.jwt.TokenProvider;

import java.time.Duration;

// 토큰 생성과 관련된 비즈니스 로직을 처리하는 서비스

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;

    // 리프레시 토큰을 전달 받아 검사 후 새 액세스 토큰을 발급해 줌
    public String createNewAccessToken(String refreshToken){
        // 토큰 유효성 검사
        if(!tokenProvider.validToken(refreshToken)){
            throw new IllegalArgumentException("Unexpected token");
        }

        Long memberNo = refreshTokenService.findByRefreshToken(refreshToken).getMemberNo();
        Member member = memberService.findById(memberNo);

        return tokenProvider.generateToken(member, Duration.ofHours(2));
    }
}
