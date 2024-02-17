package com.example.puzzle.controller;

import com.example.puzzle.domain.dto.PuzzleDto;
import com.example.puzzle.domain.model.entity.form.PuzzleForm;
import com.example.puzzle.domain.model.entity.form.PuzzlePieceOrderForm;
import com.example.puzzle.service.PuzzleService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/puzzles")
@RequiredArgsConstructor
public class PuzzleController {

  private final PuzzleService puzzleService;

  @PostMapping("/create")
  public ResponseEntity<PuzzleDto> createPuzzle(
      Principal principal, @RequestBody PuzzleForm puzzleForm) {
    return ResponseEntity.ok(puzzleService.createPuzzle(
        principal.getName(), puzzleForm));
  }

  @PutMapping("/change/name")
  public ResponseEntity<String> changePuzzleName(@RequestParam Long id,
      @RequestBody String newName) {
    puzzleService.changePuzzleName(id, newName);
    return ResponseEntity.ok("수정되었습니다.");
  }

  @PutMapping("/pieces/reorder")
  public ResponseEntity<String> reorderPiece(@RequestParam Long id,
      @RequestBody List<PuzzlePieceOrderForm> forms) {
    puzzleService.reorderPiece(id, forms);
    return ResponseEntity.ok("수정되었습니다.");
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deletePuzzle(@RequestParam Long id) {
    puzzleService.deletePuzzle(id);
    return ResponseEntity.ok("삭제되었습니다.");
  }


}
