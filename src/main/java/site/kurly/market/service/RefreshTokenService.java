package site.kurly.market.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.kurly.market.domain.RefreshToken;
import site.kurly.market.repository.RefreshTokenRepository;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
