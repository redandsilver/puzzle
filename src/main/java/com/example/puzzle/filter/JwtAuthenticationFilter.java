package com.example.puzzle.filter;

import com.example.puzzle.domain.model.entity.Member;
import com.example.puzzle.domain.model.entity.RefreshToken;
import com.example.puzzle.domain.repository.MemberRepository;
import com.example.puzzle.domain.repository.RefreshTokenRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import com.example.puzzle.security.TokenProvider;
import com.example.puzzle.service.FilterService;
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

    private final TokenProvider tokenProvider;
    private final FilterService filterService;

    @Override
    protected void doFilterInternal
            (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(SecurityContextHolder.getContext().getAuthentication() == null){
            String token = filterService.resolveTokenFromRequest(request);
            if(StringUtils.hasText(token)){
                // 이미 로그아웃한 토큰인지
                if(tokenProvider.isLogout(token)){
                    throw new CustomException(ErrorCode.ALREADY_LOGOUT_USER);
                }
                // 토큰 검증
                token = tokenProvider.checkAccessToken(token);
                Authentication auth = this.tokenProvider.getAuthentication(token);
                log.info(String.format("[%s] -> %s",
                        this.tokenProvider.getUsername(token),request.getRequestURI()));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request,response);
    }
}
