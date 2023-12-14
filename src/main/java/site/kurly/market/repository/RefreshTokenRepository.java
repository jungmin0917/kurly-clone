package site.kurly.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.kurly.market.domain.RefreshToken;

import java.util.Optional;

// 리프레시 토큰과 관련된 데이터베이스 조작을 담당하는 리포지토리

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberNo(Long memberNo);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
