package com.example.puzzle.service;

import com.example.puzzle.domain.dto.MemberDto;
import com.example.puzzle.domain.model.entity.Member;
import com.example.puzzle.domain.repository.MemberRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import java.io.IOException;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Slf4j
public class MemberService implements UserDetailsService {

  private final MemberRepository memberRepository;
  private final ImageService imageService;
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return MemberDto.from(this.memberRepository.findByNickname(username)
        .orElseThrow(() ->
            new UsernameNotFoundException("couldn't find user -> " + username)));
  }

  @Transactional
  public MemberDto updateImage(String name, MultipartFile multipartFile) throws IOException {
    Member member = memberRepository.findByNickname(name).orElseThrow(
        () -> new CustomException(ErrorCode.USER_NOT_FOUND)
    );
    String fileName = imageService.makefileName(multipartFile.getOriginalFilename());
    String awsUrl = imageService.uploadImage(multipartFile, fileName);
    member.uploadProfileImage(awsUrl);

    return MemberDto.from(member);
  }

  private String makefileName(String originalFilename, String folder) {
    return folder
        + UUID.randomUUID().toString()
        + originalFilename;
  }
}
