package com.example.puzzle.domain.model;

import com.example.puzzle.domain.model.entity.Member;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


public class Auth {
    @Data
    public static class SignIn{
        private String nickname;
        private String password;
    }
    @Data
    public static class SignUp{
        private String nickname;
        private String password;
        private String phoneNumber;
        private String profileImageUrl;

        public Member toEntity(String encodedPassword){
            return Member.builder()
                    .nickname(this.nickname)
                    .password(encodedPassword)
                    .phoneNumber(this.phoneNumber)
                    .profileImageUrl(this.profileImageUrl)
                    .roles(new ArrayList<String>())
                    .build();
        }
    }
}
