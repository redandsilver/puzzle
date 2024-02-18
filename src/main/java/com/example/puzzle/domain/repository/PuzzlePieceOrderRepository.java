package com.example.puzzle.domain.repository;

import com.example.puzzle.domain.model.entity.PuzzlePieceOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuzzlePieceOrderRepository extends JpaRepository<PuzzlePieceOrder, Long> {

  void deleteByPuzzleId(Long puzzleId);

  Optional<List<PuzzlePieceOrder>> findAllByPuzzleId(Long puzzleId);

  Optional<PuzzlePieceOrder> findByPuzzleIdAndPieceId(Long puzzleId, Long pieceId);

  Optional<Object> findByPuzzleIdAndOrderIndex(Long puzzleId, int indexOrder);
}
