package com.example.puzzle.controller;
import com.example.puzzle.domain.dto.PieceDto;
import com.example.puzzle.domain.model.entity.form.PieceForm;
import com.example.puzzle.service.PieceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/piece")
@PreAuthorize("hasRole('MEMBER')")
@RequiredArgsConstructor
public class PieceController {
    private final PieceService pieceService;
    @PostMapping("/create")
    public ResponseEntity<String> createPiece (Principal principal, @RequestBody PieceForm form){
        PieceDto pieceDto = pieceService.createPiece(principal.getName(),form);
        return ResponseEntity.ok(pieceDto.getTitle()+" 저장되었습니다.");
    }
    @PutMapping("/edit/{piece_id}")
    public ResponseEntity<String> editPiece ( @PathVariable Long pieceId, @RequestBody PieceForm form){
        PieceDto pieceDto = pieceService.editPiece(pieceId,form);
        return ResponseEntity.ok(pieceDto.getTitle()+" 수정되었습니다.");
    }

    @DeleteMapping("/delete/{piece_id}")
    public ResponseEntity<String> deletePiece (@PathVariable Long pieceId){
         pieceService.deletePiece(pieceId);
        return ResponseEntity.ok("삭제되었습니다.");
    }
}
