package site.kurly.market.dto;

import lombok.Getter;
import lombok.Setter;

// 액세스 토큰 생성 API에서 사용되는 DTO 클래스

@Getter
@Setter
public class CreateAccessTokenRequestDTO {
    private String refreshToken;
}
