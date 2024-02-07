package com.example.puzzle.controller;

import com.example.puzzle.domain.member.MemberDto;
import com.example.puzzle.domain.model.entity.Member;
import com.example.puzzle.security.TokenProvider;
import com.example.puzzle.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MemberController {
    private TokenProvider tokenProvider;
    private MemberService memberService;
    @GetMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<String> getProfileInfo(){

        return ResponseEntity.ok("welcome!");
    }


}
