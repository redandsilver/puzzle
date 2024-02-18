package com.example.puzzle.controller;

import com.example.puzzle.domain.dto.PieceDto;
import com.example.puzzle.domain.dto.PuzzleDto;
import com.example.puzzle.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

  private final SearchService searchService;

  @GetMapping("/pieces")
  public ResponseEntity<PieceDto> getPieceById(@RequestParam Long pieceId) {
    return ResponseEntity.ok(this.searchService.getPieceById(pieceId));

  }

  @GetMapping("/puzzles")
  public ResponseEntity<PuzzleDto> getPuzzleById(@RequestParam Long puzzleId) {
    return ResponseEntity.ok(this.searchService.getPuzzleById(puzzleId));

  }

}
