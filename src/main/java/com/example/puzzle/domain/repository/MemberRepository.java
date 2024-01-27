package com.example.puzzle.domain.repository;

import com.example.puzzle.domain.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Member> findByPhoneNumber(String phoneNumber);
}
