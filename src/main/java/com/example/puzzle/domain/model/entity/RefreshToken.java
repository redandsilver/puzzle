package com.example.puzzle.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


import javax.transaction.Transactional;

@Getter
@AllArgsConstructor
// redis에서 long 타입의 아이디를 key로 사용하는 두 개의 엔티티가 있을때 key가 중복 될 수 있기 떄문에
// value값을 prefix로 두어서 구분 지을 수 있다.
// 실제 redis 값에는 "refreshtoken:refreshToken값"로 key 값이 지정된다.
@RedisHash(value = "refreshtoken", timeToLive = 60*60*24*7)
public class RefreshToken {
    @Id
    private String refreshToken;
    @Indexed
    private String accessToken;
    private Long memberId;

    public void updateRefreshToken(String accessToken){
        this.accessToken = accessToken;
    }
}