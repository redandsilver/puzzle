package com.example.puzzle.service;

import com.example.puzzle.domain.dto.PieceDto;
import com.example.puzzle.domain.dto.PuzzleDto;
import com.example.puzzle.domain.model.entity.Member;
import com.example.puzzle.domain.model.entity.Piece;
import com.example.puzzle.domain.model.entity.Puzzle;
import com.example.puzzle.domain.model.entity.PuzzlePieceOrder;
import com.example.puzzle.domain.model.entity.form.PuzzleForm;
import com.example.puzzle.domain.model.entity.form.PuzzlePieceOrderForm;
import com.example.puzzle.domain.repository.MemberRepository;
import com.example.puzzle.domain.repository.PieceRepository;
import com.example.puzzle.domain.repository.PuzzlePieceOrderRepository;
import com.example.puzzle.domain.repository.PuzzleRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PuzzleService {

  private final PuzzleRepository puzzleRepository;
  private final PuzzlePieceOrderRepository puzzlePieceOrderRepository;
  private final PieceRepository pieceRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public PuzzleDto createPuzzle(
      String name, PuzzleForm puzzleForm) {

    Puzzle puzzle = Puzzle.from(puzzleForm);
    puzzle.setWriterName(name);
    for (PuzzlePieceOrderForm pieceOrderForm : puzzleForm.getPieces()) {
      Piece piece = validatePiece(name, pieceOrderForm.getPieceId());
      PuzzlePieceOrder puzzlePieceOrder = PuzzlePieceOrder.builder()
          .piece(piece)
          .orderIndex(pieceOrderForm.getIndexOrder())
          .build();
      puzzlePieceOrder.makePuzzle(puzzle, piece);
    }

    return PuzzleDto.from(puzzleRepository.save(puzzle));
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
              .orElseThrow(() -> new CustomException(ErrorCode.WRONG_PIECE_ORDER));
      puzzlePieceOrder.putPieceIntoPuzzle(puzzle, piece, form.getIndexOrder());

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

  @Transactional
  public void deletePieceInPuzzle(Long puzzleId, List<PuzzlePieceOrderForm> forms) {
    Puzzle puzzle = puzzleRepository.findById(puzzleId).orElseThrow(
        () -> new CustomException(ErrorCode.PUZZLE_NOT_EXIST)
    );

    for (PuzzlePieceOrderForm form : forms) {
      Piece piece = pieceRepository.findById(form.getPieceId()).orElseThrow(
          () -> new CustomException(ErrorCode.PIECE_NOT_EXIST)
      );
      PuzzlePieceOrder puzzlePieceOrder =
          puzzlePieceOrderRepository.findByPuzzleIdAndPieceId(puzzle.getId(), piece.getId())
              .orElseThrow(() -> new CustomException(ErrorCode.WRONG_PIECE_ORDER));
      puzzlePieceOrderRepository.delete(puzzlePieceOrder);
    }
  }

  @Transactional
  public void addPieceInPuzzle(String name, Long puzzleId, List<PuzzlePieceOrderForm> forms) {
    Puzzle puzzle = puzzleRepository.findById(puzzleId).orElseThrow(
        () -> new CustomException(ErrorCode.PUZZLE_NOT_EXIST)
    );

    for (PuzzlePieceOrderForm form : forms) {
      Piece piece = pieceRepository.findById(form.getPieceId()).orElseThrow(
          () -> new CustomException(ErrorCode.PIECE_NOT_EXIST)
      );
      validatePieceOrder(name, puzzleId, form);
      PuzzlePieceOrder pieceOrder =
          puzzlePieceOrderRepository.save(
              PuzzlePieceOrder.from(puzzle, piece, form.getIndexOrder()));
      pieceOrder.putPieceIntoPuzzle(puzzle, piece, form.getIndexOrder());
    }
  }

  private void validatePieceOrder(String name, Long puzzleId, PuzzlePieceOrderForm form) {
    if (puzzlePieceOrderRepository.findByPuzzleIdAndOrderIndex(puzzleId, form.getIndexOrder())
        .isPresent()) {
      throw new CustomException(ErrorCode.WRONG_PIECE_ORDER);
    }
  }

  private Piece validatePiece(String name, Long pieceId) {
    Piece piece = pieceRepository.findById(pieceId)
        .orElseThrow(() -> new CustomException(ErrorCode.PIECE_NOT_EXIST));
    Member member = memberRepository.findByNickname(name)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    if (!member.getPieceAuthorites().contains(pieceId)) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }
    if (member.getNickname().equals(piece.getMember().getNickname())) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }
    return piece;
  }

  public List<PieceDto> getPuzzleDetail(Long puzzleId) {
    Puzzle puzzle = puzzleRepository.findById(puzzleId).orElseThrow(
        () -> new CustomException(ErrorCode.PUZZLE_NOT_EXIST)
    );
    return puzzlePieceOrderRepository.findAllByPuzzleId(puzzleId)
        .orElseThrow(() -> new CustomException(ErrorCode.PIECE_ORDER_NOT_EXIST))
        .stream()
        .map(puzzlePieceOrder -> pieceRepository.findById(puzzlePieceOrder.getPiece().getId())
            .orElseThrow(() -> new CustomException(ErrorCode.PIECE_NOT_EXIST)))
        .map(PieceDto::from)
        .collect(Collectors.toList());
  }

}
