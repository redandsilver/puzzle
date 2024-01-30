package com.example.puzzle.domain.repository;

import com.example.puzzle.domain.model.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRespository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByAccessToken(String token);
}
