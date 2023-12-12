package site.kurly.market.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.kurly.market.repository.MemberRepository;

// 스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
// 사용자의 로그인 요청이 있을 때 해당 사용자의 정보를 데이터베이스 등에서 가져와 Spring Security에게 제공하는 역할을 함

@RequiredArgsConstructor
@Service
// 최신 스프링 버전에선 UserDetailsService를 구현한 이 클래스를 찾아 이 빈을 AuthenticationManager가 필요할 때 사용하게 됨
public class UserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository; // 생성자 주입

    // 사용자 이름(여기선 email)으로 사용자의 정보를 가져오는 메서드
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }
}
