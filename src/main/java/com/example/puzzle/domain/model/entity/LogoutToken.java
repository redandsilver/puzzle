package com.example.puzzle.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value = "blacklist", timeToLive = 60*30)
public class LogoutToken {
    @Id
    private String id;
    @Indexed
    private String accessToken;
}
