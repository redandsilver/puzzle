package com.example.puzzle.domain.repository;

import com.example.puzzle.domain.model.entity.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuzzleRepository extends JpaRepository<Puzzle,Long> {
}
