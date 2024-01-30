package com.example.puzzle.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


import javax.transaction.Transactional;


@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60*60*24*3)
public class RefreshToken {

    @Id
    private String refreshToken;
    private Long userId;

    @Indexed
    private String accessToken;

    @Transactional
    public void updateRefreshToken(String accessToken){
        this.accessToken = accessToken;
    }
}