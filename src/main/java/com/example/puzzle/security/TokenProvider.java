package com.example.puzzle.security;

import com.example.puzzle.domain.model.entity.Member;
import com.example.puzzle.domain.model.entity.RefreshToken;
import com.example.puzzle.domain.repository.LogoutTokenRepository;
import com.example.puzzle.domain.repository.MemberRepository;
import com.example.puzzle.domain.repository.RefreshTokenRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import com.example.puzzle.service.AuthService;
import com.example.puzzle.service.MemberService;
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

    private final MemberService memberService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LogoutTokenRepository logoutTokenRepository;

    private final MemberRepository memberRepository;

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

    public void generateRefreshToken(String accessToken, Long memberId){
        RefreshToken token =
                new RefreshToken(UUID.randomUUID().toString(),accessToken,memberId);
        refreshTokenRepository.save(token);
    }

    public Authentication getAuthentication(String jwt){
        UserDetails userDetails
                = this.memberService.loadUserByUsername(this.getUsername(jwt));

        return new UsernamePasswordAuthenticationToken(
                userDetails,"",userDetails.getAuthorities());
    }

    public String getUsername(String token){

        return this.parseClaims(token).getSubject();
    }

    public String checkAccessToken(String token){
        var claims = this.parseClaims(token);
        var now = new Date();
        if(claims.getExpiration().before(now)){
            token = checkRefreshToken(token);
        }
        return token;
    }

    public Claims parseClaims(String token){
        try{
            return Jwts.parser().setSigningKey(this.secretKey)
                    .parseClaimsJws(token).getBody();

        }catch (ExpiredJwtException e){
            return e.getClaims();
        }

    }
    public boolean isLogout(String token) {

        return logoutTokenRepository.findByAccessToken(token).isPresent();
    }

    public String checkRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(token)
                .orElseThrow(()-> new CustomException(ErrorCode.TOKEN_EXPIRED));
        Member member = memberRepository.findById(refreshToken.getMemberId())
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 토큰 재발급
        String newToken = generateToken(member.getNickname(),member.getRoles());
        refreshTokenRepository.save(new RefreshToken(refreshToken.getRefreshToken(),token,refreshToken.getMemberId()));
        return newToken;
    }
}
