package com.example.puzzle.domain.member;

import com.example.puzzle.domain.model.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String nickname;

    public static MemberDto from (Member member){
        return new MemberDto(member.getId(), member.getNickname());
    }
}
