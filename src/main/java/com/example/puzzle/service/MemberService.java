package com.example.puzzle.service;

import com.example.puzzle.domain.dto.MemberDto;
import com.example.puzzle.domain.dto.PieceDto;
import com.example.puzzle.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Slf4j
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return MemberDto.from(this.memberRepository.findByNickname(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("couldn't find user -> "+username)));
    }

}
