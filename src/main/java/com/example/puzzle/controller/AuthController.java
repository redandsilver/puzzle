package com.example.puzzle.controller;
import com.example.puzzle.domain.dto.MemberDto;
import com.example.puzzle.domain.model.entity.form.Auth;
import com.example.puzzle.security.TokenProvider;
import com.example.puzzle.service.AuthService;
import com.example.puzzle.service.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final FilterService filterService;

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

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
        var member = MemberDto.from(authService.authenticate(form));
        var accessToken = tokenProvider.generateToken(member.getUsername(), member.getRoles());
        tokenProvider.generateRefreshToken(accessToken, member.getId());
        response.setHeader(TOKEN_HEADER, TOKEN_PREFIX+accessToken);
        return ResponseEntity.ok(accessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout (HttpServletRequest request){
        var token = filterService.resolveTokenFromRequest(request);
        authService.logout(token);
        return ResponseEntity.ok("로그아웃");
    }
}
