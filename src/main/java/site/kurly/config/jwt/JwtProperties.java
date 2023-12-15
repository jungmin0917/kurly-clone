package site.kurly.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt") // 자바 클래스에 해당 프로퍼티값을 가져와서 사용하는 어노테이션
// 이 경우, jwt.issuer, jwt.secret_key 등을 가져올 수 있다.
public class JwtProperties {
    private String issuer; // 토큰 발급자
    private String secretKey; // 자동으로 스네이크 케이스가 캐멀 케이스로 매핑된다.
}
