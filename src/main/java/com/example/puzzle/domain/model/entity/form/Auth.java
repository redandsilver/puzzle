package com.example.puzzle.domain.model.entity.form;

import com.example.puzzle.domain.model.entity.Member;
import java.util.ArrayList;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;


public class Auth {


  @Data
  public static class SignIn {

    private String nickname;
    private String password;
  }

  @Data
  public static class SignUp {

    @Value("${default.image}")
    private String profileImage;
    private String nickname;
    private String password;
    private String phoneNumber;

    public Member toEntity(String encodedPassword) {
      return Member.builder()
          .nickname(this.nickname)
          .password(encodedPassword)
          .phoneNumber(this.phoneNumber)
          .profileImageUrl(this.profileImage)
          .roles(new ArrayList<String>())
          .build();
    }
  }
}
