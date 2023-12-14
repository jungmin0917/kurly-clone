package site.kurly.market.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.kurly.market.domain.RefreshToken;
import site.kurly.market.repository.RefreshTokenRepository;

// 리프레시 토큰과 관련된 비즈니스 로직을 처리하는 서비스

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
