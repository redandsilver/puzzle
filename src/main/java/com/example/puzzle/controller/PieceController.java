package com.example.puzzle.controller;

import com.example.puzzle.domain.dto.PieceDto;
import com.example.puzzle.domain.model.entity.form.PieceForm;
import com.example.puzzle.service.PieceService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pieces")
@PreAuthorize("hasRole('MEMBER')")
@RequiredArgsConstructor
public class PieceController {

  private final PieceService pieceService;

  @PostMapping("/create")
  public ResponseEntity<String> createPiece(
      Principal principal,
      @RequestBody PieceForm form) {
    PieceDto pieceDto = pieceService.createPiece(principal.getName(), form);
    return ResponseEntity.ok(pieceDto.getTitle() + " 저장되었습니다.");
  }

  @PostMapping("/{pieceId}/upload/images")
  public ResponseEntity<String> uploadImages(
      Principal principal, @PathVariable long pieceId,
      @RequestParam("file") List<MultipartFile> multipartFiles) {
    pieceService.uploadImages(pieceId, multipartFiles);
    return ResponseEntity.ok("사진이 저장되었습니다.");
  }


  @PutMapping("/edit")
  public ResponseEntity<String> editPiece(Principal principal, @RequestParam Long pieceId,
      @RequestBody PieceForm form) {
    PieceDto pieceDto = pieceService.editPiece(pieceId, form);
    return ResponseEntity.ok(pieceDto.getTitle() + " 수정되었습니다.");
  }

  @DeleteMapping("/{pieceId}/delete")
  public ResponseEntity<String> deleteImage(Principal principal, @PathVariable Long pieceId,
      @RequestParam String fileName) {
    pieceService.deleteImage(pieceId, fileName);
    return ResponseEntity.ok("사진이 삭제되었습니다.");
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deletePiece(@RequestParam Long pieceId) {
    pieceService.deletePiece(pieceId);
    return ResponseEntity.ok("삭제되었습니다.");
  }

  @PutMapping("/take")
  public ResponseEntity<String> takePiece(Principal principal, @RequestParam Long pieceId) {
    pieceService.takePiece(principal.getName(), pieceId);
    return ResponseEntity.ok("조각을 떼왔습니다.");
  }
}
