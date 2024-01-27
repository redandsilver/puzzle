package com.example.puzzle.service;

import com.example.puzzle.domain.model.Auth;
import com.example.puzzle.domain.model.entity.Member;
import com.example.puzzle.domain.repository.MemberRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
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
        if(member.getVerificationCode().equals(code)){
            if(member.getVerifyExpiredAt().isBefore(LocalDateTime.now())){
                throw new CustomException(ErrorCode.EXPIRED_CODE);
            }
            member.setVerify(true);
        }else{
            throw new CustomException(ErrorCode.WRONG_CODE);
        }
    }
}
