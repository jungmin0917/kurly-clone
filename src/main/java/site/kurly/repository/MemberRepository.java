package site.kurly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.kurly.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
