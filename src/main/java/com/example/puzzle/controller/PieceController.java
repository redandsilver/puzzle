package com.example.puzzle.controller;

import com.example.puzzle.domain.dto.PieceDto;
import com.example.puzzle.domain.model.entity.form.PieceForm;
import com.example.puzzle.service.PieceService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pieces")
@PreAuthorize("hasRole('MEMBER')")
@RequiredArgsConstructor
public class PieceController {

  private final PieceService pieceService;

  @PostMapping("/create")
  public ResponseEntity<String> createPiece(Principal principal, @RequestBody PieceForm form) {
    PieceDto pieceDto = pieceService.createPiece(principal.getName(), form);
    return ResponseEntity.ok(pieceDto.getTitle() + " 저장되었습니다.");
  }

  @PutMapping("/edit")
  public ResponseEntity<String> editPiece(@RequestParam Long pieceId, @RequestBody PieceForm form) {
    PieceDto pieceDto = pieceService.editPiece(pieceId, form);
    return ResponseEntity.ok(pieceDto.getTitle() + " 수정되었습니다.");
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deletePiece(@RequestParam Long pieceId) {
    pieceService.deletePiece(pieceId);
    return ResponseEntity.ok("삭제되었습니다.");
  }
}
