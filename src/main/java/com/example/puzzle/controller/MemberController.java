package com.example.puzzle.controller;

import com.example.puzzle.domain.dto.MemberDto;
import com.example.puzzle.service.MemberService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/mypage")
@PreAuthorize("hasRole('MEMBER')")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @GetMapping
  public ResponseEntity<MemberDto> getProfileInfo(Authentication authentication) {
    log.info(authentication.getName());
    return ResponseEntity.ok(
        (MemberDto) memberService.loadUserByUsername(authentication.getName()));
  }

  @PutMapping("/image")
  public ResponseEntity<MemberDto> updateProfileImage(
      Authentication authentication, @RequestParam("file") MultipartFile multipartFile)
      throws IOException {
    log.info(authentication.getName());
    return ResponseEntity.ok((memberService.updateImage(authentication.getName(), multipartFile)));
  }
}
