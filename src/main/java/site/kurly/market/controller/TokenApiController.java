package site.kurly.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.kurly.market.dto.CreateAccessTokenRequestDTO;
import site.kurly.market.dto.CreateAccessTokenResponseDTO;
import site.kurly.market.service.TokenService;

// 액세스 토큰을 생성하는 API를 제공하는 컨트롤러. 주로 리프레시 토큰을 이용해 새로운 액세스 토큰을 생성하는 역할을 한다.

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponseDTO> createNewAccessToken(@RequestBody CreateAccessTokenRequestDTO request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return new ResponseEntity<>(new CreateAccessTokenResponseDTO(newAccessToken), HttpStatus.CREATED);
    }
}
