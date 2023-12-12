package site.kurly.market.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
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

    // 고유식별번호 (카카오로 받아온 id를 넣을 것임)
    @Column(name = "member_auth", unique = true, nullable = false)
    private String auth;

    // 이메일
    @Column(name = "member_email", unique = true, nullable = false)
    private String email;

    // 닉네임
    @Column(name = "member_nickname", nullable = false)
    private String nickname;

    // 비밀번호
    @Column(name = "member_password")
    private String password;

    // 이름
    @Column(name = "member_name")
    private String name;

    // 전화번호
    @Column(name = "member_phone_number")
    private String phoneNumber;

    // 기본주소
    @Column(name = "member_address")
    private String address;

    // 상세주소
    @Column(name = "member_address_detail")
    private String addressDetail;

    // 우편번호
    @Column(name = "member_address_postcode")
    private String addressPostcode;

    // 성별
    @Column(name = "member_gender")
    private String gender;

    // 생일
    @Column(name = "member_birthday")
    private LocalDateTime birthday;

    // 등급 (0: 일반회원, 5: 관리자)
    @Column(name = "member_grade")
    private int grade;

    // 적립금
    @Column(name = "member_reward")
    private int reward;

    // 탈퇴여부
    @Column(name = "member_withdraw")
    private boolean withdraw;

    // Builder를 통한 Member 엔티티 생성
    @Builder
    public Member(String auth, String email, String password, String nickname, String name, String phoneNumber, String address, String addressDetail, String addressPostcode, String gender, LocalDateTime birthday, int grade, List<String> roles) {
        this.auth = auth;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.addressDetail = addressDetail;
        this.addressPostcode = addressPostcode;
        this.gender = gender;
        this.birthday = birthday;
        this.grade = grade;
        this.roles = roles != null ? roles : new ArrayList<>();
    }

    // 권한 리스트 (스프링 시큐리티용)
    // member_roles라는 테이블이 생기며, member 테이블과 연동되어 있다.
    // @ElementCollection 을 이용하면
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_no"))
    private List<String> roles = new ArrayList<>();

    // 권한 리스트를 반환 (스프링 시큐리티용)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    // password 반환 (스프링 시큐리티용)
    @Override
    public String getPassword() {
        return this.password;
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








