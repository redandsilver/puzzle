package com.example.puzzle.service;

import com.example.puzzle.domain.model.Auth;
import com.example.puzzle.domain.model.entity.Member;
import com.example.puzzle.domain.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.transaction.Transactional;
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private AuthService authService;

    @Test
    @Transactional
    void signUpMemberSuccess(){
        //given
        Auth.SignUp form = new Auth.SignUp();
        form.setEmail("testmail");
        form.setNickname("nickname");
        form.setPassword("1342sda");
        // when
        Member member = authService.signUp(form);
        member.setId(1L);
        // then
        Assertions.assertEquals(member.getEmail(),memberRepository.findById(1L).get().getEmail());
        Assertions.assertEquals(member.getNickname(),memberRepository.findById(1L).get().getNickname());
        Assertions.assertEquals(member.getPassword(),memberRepository.findById(1L).get().getPassword());
        Assertions.assertEquals(member.getVerificationCode(),memberRepository.findById(1L).get().getVerificationCode());
        Assertions.assertEquals(member.getVerifyExpiredAt(),memberRepository.findById(1L).get().getVerifyExpiredAt());
    }

}