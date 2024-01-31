package com.example.puzzle.security;

import com.example.puzzle.config.util.RedisUtil;
import com.example.puzzle.domain.model.entity.Member;
import com.example.puzzle.domain.model.entity.RefreshToken;
import com.example.puzzle.domain.repository.RefreshTokenRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import com.example.puzzle.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {
    private static final long TOKEN_EXPIRE_TIME = 1000*60*30; // 30 min
    private static final String KEY_ROLES = "roles";

    private final AuthService authService;
    private final RefreshTokenRepository refreshTokenRespository;
    private final RedisUtil redisUtil;
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

    public void generateRefreshToken(Member member, String accessToken){

        var refreshToken = UUID.randomUUID().toString();
        RefreshToken redis =
                new RefreshToken(refreshToken,member.getId(),accessToken);
        refreshTokenRespository.save(redis);
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

    public boolean validateToken(String token){
        var claims = this.parseClaims(token);
        var now = new Date();
        return !claims.getExpiration().before(now);
    }

    public Claims parseClaims(String token){
        try{
            return Jwts.parser().setSigningKey(this.secretKey)
                    .parseClaimsJws(token).getBody();

        }catch (ExpiredJwtException e){
            return e.getClaims();
        }

    }

    public Date getExpiration(String token) {
        return this.parseClaims(token).getExpiration();
    }

    public boolean isLogout(String token) {
        if (redisUtil.hasKeyBlackList(token)){
            return true;
        }
        return false;
    }


}
