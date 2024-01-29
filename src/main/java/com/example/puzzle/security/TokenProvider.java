package com.example.puzzle.security;

import com.example.puzzle.service.AuthService;
import com.example.puzzle.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private static final long TOKEN_EXPIRE_TIME = 1000*60*60*2; // 2 hour
    private static final String KEY_ROLES = "roles";

    private final AuthService authService;

    @Value("{spring.jwt.secret}")
    private String secretKey;

    /**
     * 토큰 생성(발급)
     * @param username
     * @param roles
     * @return
     */
    public String generateToken(String username, List<String> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES,roles);

        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(expiredDate) // 토큰 완료 시간
                .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 암호화 알고리즘, 비밀키
                .compact();
    }

    public Authentication getAuthentication(String jwt){
        UserDetails userDetails
                = this.authService.loadUserByUsername(this.getUsername(jwt));

        return new UsernamePasswordAuthenticationToken(
                userDetails,"",userDetails.getAuthorities());
    }

    public String getUsername(String token){

        return this.parseClaims(token).getSubject();
    }

    /**
     * 토큰이 비었는가
     * 토큰이 만료되었는가
     * @param token
     * @return
     */
    public boolean validateToken(String token){
        if(!StringUtils.hasText(token)) return false;

        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token){
        try{
            return Jwts.parser().setSigningKey(this.secretKey)
                    .parseClaimsJws(token).getBody();

        }catch (ExpiredJwtException e){
            return e.getClaims();
        }

    }

}
