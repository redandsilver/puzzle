package com.example.puzzle.controller;

import com.example.puzzle.domain.member.MemberDto;
import com.example.puzzle.domain.model.Auth;
import com.example.puzzle.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp (@RequestBody Auth.SignUp form){
        MemberDto memberDto = MemberDto.from(authService.signUp(form));
        return ResponseEntity.ok("인증코드 발송 클릭.");
    }
    @PostMapping("/sendSMS")
    public ResponseEntity<String> sendSMS (@RequestParam String phoneNumber){
        return ResponseEntity.ok(authService.sendSMS(phoneNumber));
    }
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode
            (@RequestParam String phoneNumber, @RequestParam String code){
        return ResponseEntity.ok(authService.verifyCode(phoneNumber,code));
    }
}
