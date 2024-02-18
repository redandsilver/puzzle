package com.example.puzzle.domain.repository;

import com.example.puzzle.domain.model.entity.Piece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PieceRepository extends JpaRepository<Piece, Long> {
}
