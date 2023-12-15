package site.kurly.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 액세스 토큰 생성 API에서 사용되는 DTO 클래스

@AllArgsConstructor
@Getter
public class CreateAccessTokenResponseDTO {
    private String accessToken;
}
