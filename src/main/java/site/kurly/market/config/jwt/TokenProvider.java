package site.kurly.market.config.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import site.kurly.market.domain.Member;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    private final JwtProperties jwtProperties;

    // 회원 객체와 토큰 유효기간을 받아 JWT 토큰을 생성해 반환함.
    public String generateToken(Member member, Duration expiredAt) {
        return makeToken(new Date(new Date().getTime() + expiredAt.toMillis()), member);
    }

    // JWT 토큰 생성 메서드
    private String makeToken(Date expiry, Member member) {
        return Jwts.builder() // JWT 빌더 생성
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 type을 "JWT"로 설정함.
                .setIssuer(jwtProperties.getIssuer()) // 내용 iss : 발급자
                .setIssuedAt(new Date()) // 내용 iat : 현재 시간
                .setExpiration(expiry) // 내용 exp : 매개변수로 넘어온 expiry
                .setSubject(member.getEmail()) // 내용 sub : 유저의 이메일
                .claim("id", member.getNo()) // 비공개 클레임 id : 유저 No
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) // 알고리즘과 비밀키값을 지정해주어 서명을 만든다.
                .compact(); // URL-safe한 JWT 문자열 (즉, 토큰)을 반환함
    }

    // JWT 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
        try { // 여기서 제대로 통과했다는 건 토큰이 유효하다는 것
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        // 토큰으로부터 클레임을 가져옴.
        Claims claims = getClaims(token);

        // 권한을 가지는 권한 Set 자료구조를 만듦.
        // Collections.singleton() 메서드는 설정한 요소만 포함한 Set 자료구조를 만들어 줌
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        // 권한이 여러 개 들어있는 자료구조와 토큰을 이용해 인증 정보를 가져옴
        // 이 때, 프로젝트에서 만든 User 클래스가 아닌 스프링 시큐리티에서 제공하는 객체은 User 클래스를 임포트해야 한다.
        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
    }

    // 토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    // 토큰으로부터 클레임을 조회하는 메서드
    private Claims getClaims(String token) {
        return Jwts.parser() // JWT 파서 생성
                .setSigningKey(jwtProperties.getSecretKey()) // 토큰 서명 검정 시 사용할 비밀키를 설정함
                .parseClaimsJws(token) // 토큰을 파싱하고, getBody()로 클레임을 얻는다.
                .getBody(); // 얻어진 클레임을 반환한다.
    }

}












