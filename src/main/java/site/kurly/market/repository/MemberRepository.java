package site.kurly.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.kurly.market.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
