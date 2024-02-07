package com.example.puzzle.security;

import com.example.puzzle.domain.model.entity.Member;
import com.example.puzzle.domain.model.entity.RefreshToken;
import com.example.puzzle.domain.repository.MemberRepository;
import com.example.puzzle.domain.repository.RefreshTokenRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;


    @Override
    protected void doFilterInternal
            (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(SecurityContextHolder.getContext().getAuthentication() == null){
            String token = this.resolveTokenFromRequest(request);
            if(StringUtils.hasText(token)){
                // 이미 로그아웃한 토큰인지
                if(tokenProvider.isLogout(token)){
                    throw new CustomException(ErrorCode.TOKEN_EXPIRED);
                }
                // 토큰이 만료됐다면
                if(!tokenProvider.validateToken(token)){
                    RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(token)
                            .orElseThrow(()-> new CustomException(ErrorCode.TOKEN_EXPIRED));

                    Member member = memberRepository.findById(refreshToken.getMemberId())
                            .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
                    // 토큰 재발급
                    token = tokenProvider.generateToken(member.getNickname(),member.getRoles());
                    refreshTokenRepository.save(new RefreshToken(refreshToken.getRefreshToken(),token,refreshToken.getMemberId()));
                }
                // 토큰 유효성 검증
                Authentication auth = this.tokenProvider.getAuthentication(token);
                log.info(String.format("[%s] -> %s",
                        this.tokenProvider.getUsername(token),request.getRequestURI()));
                SecurityContextHolder.getContext().setAuthentication(auth);

            }
        }
        // filter가 연속적으로 실행이 되도록
        filterChain.doFilter(request,response);
    }
    public String resolveTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader(TOKEN_HEADER);
        if(!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)){
            return token.substring(TOKEN_PREFIX.length()); // 실제 토큰 부위
        }
        return null;
    }
}
