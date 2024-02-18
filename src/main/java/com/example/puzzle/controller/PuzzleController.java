package com.example.puzzle.controller;

import com.example.puzzle.domain.dto.PieceDto;
import com.example.puzzle.domain.dto.PuzzleDto;
import com.example.puzzle.domain.model.entity.form.PuzzleForm;
import com.example.puzzle.domain.model.entity.form.PuzzlePieceOrderForm;
import com.example.puzzle.service.PuzzleService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/puzzles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MEMBER')")
public class PuzzleController {

  private final PuzzleService puzzleService;

  @GetMapping
  public ResponseEntity<List<PieceDto>> getPuzzleDetail(
      Principal principal, @RequestParam Long puzzleId) {
    return ResponseEntity.ok(puzzleService.getPuzzleDetail(puzzleId));
  }

  @PostMapping("/create")
  public ResponseEntity<PuzzleDto> createPuzzle(
      Principal principal, @RequestBody PuzzleForm puzzleForm) {
    return ResponseEntity.ok(puzzleService.createPuzzle(
        principal.getName(), puzzleForm));
  }

  @PutMapping("/change/name")
  public ResponseEntity<String> changePuzzleName(@RequestParam Long puzzleId,
      @RequestBody String newName) {
    puzzleService.changePuzzleName(puzzleId, newName);
    return ResponseEntity.ok("수정되었습니다.");
  }

  @PutMapping("/reorder/pieces")
  public ResponseEntity<String> reorderPiece(@RequestParam Long puzzleId,
      @RequestBody List<PuzzlePieceOrderForm> forms) {
    puzzleService.reorderPiece(puzzleId, forms);
    return ResponseEntity.ok("조각을 다시 맞췄습니다.");
  }

  @DeleteMapping("/delete/pieces")
  public ResponseEntity<String> deletePieceInPuzzle(@RequestParam Long puzzleId,
      @RequestBody List<PuzzlePieceOrderForm> forms) {
    puzzleService.deletePieceInPuzzle(puzzleId, forms);
    return ResponseEntity.ok("조각을 뺐습니다.");
  }

  @PostMapping("/add/pieces/")
  public ResponseEntity<String> addPieceInPuzzle(Principal principal, @RequestParam Long puzzleId,
      @RequestBody List<PuzzlePieceOrderForm> forms) {
    puzzleService.addPieceInPuzzle(principal.getName(), puzzleId, forms);
    return ResponseEntity.ok("조각을 끼웠습니다.");
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deletePuzzle(@RequestParam Long puzzleId) {
    puzzleService.deletePuzzle(puzzleId);
    return ResponseEntity.ok("퍼즐을 해체했습니다.");
  }
}
