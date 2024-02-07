package com.example.puzzle.service;

import com.example.puzzle.domain.repository.MemberRepository;
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

    }

}