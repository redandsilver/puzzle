package com.example.puzzle.domain.repository;

import com.example.puzzle.domain.model.entity.LogoutToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogoutTokenRepository extends CrudRepository<LogoutToken, String> {
    Optional<LogoutToken> findByAccessToken(String token);
}

