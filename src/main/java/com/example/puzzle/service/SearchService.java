package com.example.puzzle.service;

import com.example.puzzle.domain.dto.PieceDto;
import com.example.puzzle.domain.dto.PuzzleDto;
import com.example.puzzle.domain.model.entity.Piece;
import com.example.puzzle.domain.model.entity.Puzzle;
import com.example.puzzle.domain.repository.PieceRepository;
import com.example.puzzle.domain.repository.PuzzleRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final PieceRepository pieceRepository;
  private final PuzzleRepository puzzleRepository;
  private final Trie trie;

  public PieceDto getPieceById(Long pieceId) {
    Piece piece = pieceRepository.findById(pieceId).orElseThrow(
        () -> new CustomException(ErrorCode.PIECE_NOT_EXIST)
    );
    return PieceDto.from(piece);
  }

  public PuzzleDto getPuzzleById(Long puzzleId) {
    Puzzle puzzle = puzzleRepository.findById(puzzleId).orElseThrow(
        () -> new CustomException(ErrorCode.PUZZLE_NOT_EXIST)
    );
    return PuzzleDto.from(puzzle);
  }

  public void addAutocompleteKeyword(String keyword) {
    this.trie.put(keyword, null);
  }

}
