package com.example.puzzle.service;

import com.example.puzzle.domain.dto.PuzzleDto;
import com.example.puzzle.domain.model.entity.Piece;
import com.example.puzzle.domain.model.entity.Puzzle;
import com.example.puzzle.domain.model.entity.PuzzlePieceOrder;
import com.example.puzzle.domain.model.entity.form.PuzzleForm;
import com.example.puzzle.domain.model.entity.form.PuzzlePieceOrderForm;
import com.example.puzzle.domain.repository.PieceRepository;
import com.example.puzzle.domain.repository.PuzzlePieceOrderRepository;
import com.example.puzzle.domain.repository.PuzzleRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PuzzleService {

  private final PuzzleRepository puzzleRepository;
  private final PuzzlePieceOrderRepository puzzlePieceOrderRepository;
  private final PieceRepository pieceRepository;

  @Transactional
  public PuzzleDto createPuzzle(
      String name, PuzzleForm puzzleForm) {

    Puzzle puzzle = Puzzle.from(puzzleForm);
    puzzle.setWriterName(name);
    for (PuzzlePieceOrderForm pieceOrderForm : puzzleForm.getPieces()) {
      Piece piece = pieceRepository.findById(pieceOrderForm.getPieceId())
          .orElseThrow(() -> new CustomException(ErrorCode.PIECE_NOT_EXIST));
      PuzzlePieceOrder puzzlePieceOrder = PuzzlePieceOrder.builder()
          .piece(piece)
          .orderIndex(pieceOrderForm.getIndexOrder())
          .build();
      puzzlePieceOrder.makePuzzle(puzzle, piece);
    }
    puzzle = puzzleRepository.save(puzzle);
    return PuzzleDto.from(puzzle);
  }

  @Transactional
  public void changePuzzleName(Long puzzleId, String newname) {
    Puzzle puzzle = puzzleRepository.findById(puzzleId).orElseThrow(
        () -> new CustomException(ErrorCode.PUZZLE_NOT_EXIST)
    );
    puzzle.updatePuzzleName(newname);
  }

  @Transactional
  public void reorderPiece(Long puzzleId, List<PuzzlePieceOrderForm> forms) {
    Puzzle puzzle = puzzleRepository.findById(puzzleId).orElseThrow(
        () -> new CustomException(ErrorCode.PUZZLE_NOT_EXIST)
    );
    puzzle.setPuzzleName(puzzle.getPuzzleName());

    for (PuzzlePieceOrderForm form : forms) {
      Piece piece = pieceRepository.findById(form.getPieceId()).orElseThrow(
          () -> new CustomException(ErrorCode.PIECE_NOT_EXIST)
      );
      PuzzlePieceOrder puzzlePieceOrder =
          puzzlePieceOrderRepository.findByPuzzleIdAndPieceId(puzzle.getId(), piece.getId())
              .orElseThrow(() -> new CustomException(ErrorCode.PIECE_ORDER_NOT_EXIST));
      puzzlePieceOrder.update(puzzle, piece, form);

    }
  }

  @Transactional
  public void deletePuzzle(Long puzzleId) {
    Puzzle puzzle = puzzleRepository.findById(puzzleId).orElseThrow(
        () -> new CustomException(ErrorCode.PUZZLE_NOT_EXIST)
    );
    puzzlePieceOrderRepository.deleteByPuzzleId(puzzleId);
    puzzleRepository.delete(puzzle);
  }
}
