package site.kurly.market.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {

    // 회원번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no", updatable = false)
    private Long no;

    // 고유식별번호
    @Column(name = "member_auth", unique = true)
    private String auth;

    // 이메일
    @Column(name = "member_email", unique = true)
    private String email;

    // 이름
    @Column(name = "member_name")
    private String name;

    // 전화번호
    @Column(name = "member_phone_number")
    private String phoneNumber;

    // 연령대
    @Column(name = "member_age_range")
    private String ageRange;

    // 성별
    @Column(name = "member_gender")
    private String gender;

    // 등급 (0: 일반회원, 5: 관리자)
    @Column(name = "member_grade")
    private int grade;

    // 적립금
    @Column(name = "member_reward")
    private int reward;

    // 탈퇴여부
    @Column(name = "member_withdraw")
    private boolean withdraw;

    // 권한 리스트 (스프링 시큐리티용)
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    // 권한 리스트를 반환 (스프링 시큐리티용)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    // password 반환 (스프링 시큐리티용)
    @Override
    public String getPassword() {
        return null; // OAuth2 로그인을 할 거라 비워놓음
    }

    // username 반환 (스프링 시큐리티용)
    @Override
    public String getUsername() {
        return this.auth;
    }

    // 계정 만료 여부 반환 (스프링 시큐리티용)
    @Override
    public boolean isAccountNonExpired() {
        return true; // 기본적으로 만료되지 않음
    }

    // 계정 잠금 여부 반환 (스프링 시큐리티용)
    @Override
    public boolean isAccountNonLocked() {
        return true; // 기본적으로 잠기지 않음
    }

    // 패스워드 만료 여부 반환 (스프링 시큐리티용)
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 기본적으로 만료되지 않음
    }

    // 계정 사용 여부 반환 (스프링 시큐리티용)
    @Override
    public boolean isEnabled() {
        return true; // 기본적으로 사용 가능함
    }
}








