package site.kurly.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.kurly.market.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberNo(Long memberNo);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
