package com.example.puzzle.controller;

import com.example.puzzle.domain.member.MemberDto;
import com.example.puzzle.domain.model.Auth;
import com.example.puzzle.domain.model.entity.RefreshToken;
import com.example.puzzle.domain.repository.RefreshTokenRespository;
import com.example.puzzle.security.TokenProvider;
import com.example.puzzle.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static com.example.puzzle.security.JwtAuthenticationFilter.TOKEN_HEADER;
import static com.example.puzzle.security.JwtAuthenticationFilter.TOKEN_PREFIX;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRespository refreshTokenRespository;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp (@RequestBody Auth.SignUp form){
        MemberDto memberDto = MemberDto.from(authService.signUp(form));
        return ResponseEntity.ok("인증코드 발송 클릭.");
    }
    @PostMapping("/sendSMS")
    public ResponseEntity<String> sendSMS (@RequestParam String phoneNumber){
        authService.sendSMS(phoneNumber);
        return ResponseEntity.ok("인증번호를 발송하였습니다.");
    }
    @PutMapping("/verify")
    public ResponseEntity<String> verifyCode
            (@RequestParam String phoneNumber, @RequestParam String code){
        authService.verifyCode(phoneNumber,code);
        return ResponseEntity.ok("인증완료.");
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn (@RequestBody Auth.SignIn form, HttpServletResponse response){
        var member = authService.authenticate(form);
        var accessToken = tokenProvider.generateToken(member.getUsername(), member.getRoles());
        tokenProvider.generateRefreshToken(member,accessToken);
        response.setHeader(TOKEN_HEADER, TOKEN_PREFIX+accessToken);
        return ResponseEntity.ok(accessToken);
    }
}
