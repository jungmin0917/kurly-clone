package site.kurly.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 액세스 토큰이 만료되었을 때 새로운 액세스 토큰을 발급받을 수 있도록 도와주는 클래스. 리프레시 토큰과 관련된 정보를 저장하고 업데이트하는 역할을 한다.

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_no", updatable = false)
    private Long tokenNo;

    @Column(name = "member_no", nullable = false, unique = true)
    private Long memberNo;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public RefreshToken(Long memberNo, String refreshToken) {
        this.memberNo = memberNo;
        this.refreshToken = refreshToken;
    }

    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }
}
