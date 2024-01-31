package com.example.puzzle.service;

import com.example.puzzle.config.util.RedisUtil;
import com.example.puzzle.domain.model.Auth;
import com.example.puzzle.domain.model.constants.Role;
import com.example.puzzle.domain.model.entity.Member;
import com.example.puzzle.domain.model.entity.RefreshToken;
import com.example.puzzle.domain.repository.MemberRepository;
import com.example.puzzle.domain.repository.RefreshTokenRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;

import com.example.puzzle.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.time.LocalDateTime;



@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;

    private final DefaultMessageService messageService;
    private final Message message;


    @Transactional
    public Member signUp (Auth.SignUp form){
        if(memberRepository.existsByPhoneNumber(form.getPhoneNumber())){
            throw new CustomException(ErrorCode.ALREADY_EXIST_PHONE_NUMBER);
        }
        String code = getRandomCode();
        String encodedPassword = this.passwordEncoder.encode(form.getPassword());
        Member member = memberRepository.save(form.toEntity(encodedPassword));
        changeMemberVerificationCode(member,code);
        return member;
    }
    private void changeMemberVerificationCode(Member member, String code){
        member.setVerificationCode(code);
        member.setVerifyExpiredAt(LocalDateTime.now().plusMinutes(3));
    }


    private String getRandomCode() {

        return RandomStringUtils.random(10,true,true);
    }

    public SingleMessageSentResponse sendSMS(String phoneNumber) {
        Member member = memberRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                ()-> new CustomException(ErrorCode.NOT_EXIST_PHONE_NUMBER)
        );
        message.setTo(phoneNumber);
        message.setText(message.getText()+member.getVerificationCode());
        return messageService.sendOne(new SingleMessageSendingRequest(message));
    }
    @Transactional
    public void verifyCode(String phoneNumber, String code) {
        Member member = memberRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                ()-> new CustomException(ErrorCode.NOT_EXIST_PHONE_NUMBER)
        );
        if(!member.getVerificationCode().equals(code)){
            throw new CustomException(ErrorCode.WRONG_CODE);
        }
        if(member.getVerifyExpiredAt().isBefore(LocalDateTime.now())){
            throw new CustomException(ErrorCode.EXPIRED_CODE);
        }
        member.setVerify(true);
        member.addRole(String.valueOf(Role.ROLE_MEMBER));
    }

    public Member authenticate(Auth.SignIn form) {
        var user = memberRepository.findByNickname(form.getNickname())
                .orElseThrow(()->new CustomException(ErrorCode.WRONG_LOGIN_INFO));
        if(!this.passwordEncoder.matches(form.getPassword(), user.getPassword())){
            throw new CustomException(ErrorCode.WRONG_LOGIN_INFO);
        }
        if(!user.verified()){
            throw new CustomException(ErrorCode.NOT_VERIFIED_USER);
        }
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByNickname(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("couldn't find user -> "+username));
    }

    public void logout(String token, Date expirateDate) {
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(token)
                .orElseThrow(()-> new CustomException(ErrorCode.TOKEN_NOT_FOUND));
        refreshTokenRepository.deleteById(refreshToken.getRefreshToken());
        redisUtil.setBlackList(token, "access_token", expirateDate.getTime());
    }
}
