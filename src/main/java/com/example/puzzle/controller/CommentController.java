package com.example.puzzle.controller;

import com.example.puzzle.domain.dto.CommentDto;
import com.example.puzzle.domain.model.entity.form.CommentForm;
import com.example.puzzle.service.CommentService;
import java.security.Principal;
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
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/create")
  public ResponseEntity<String> createComment(
      Principal principal, @RequestBody CommentForm form) {
    CommentDto commentDto = commentService.createComment(principal.getName(), form);
    return ResponseEntity.ok(commentDto.getWriterName() + " 님이 댓글을 남겼습니다.");
  }

  @PutMapping("/edit")
  public ResponseEntity<String> editComment(
      @RequestParam Long commentId, @RequestBody CommentForm form) {
    commentService.editComment(commentId, form);
    return ResponseEntity.ok("수정되었습니다.");
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteComment(
      @RequestParam Long commentId) {
    commentService.deleteComment(commentId);
    return ResponseEntity.ok("삭제되었습니다.");
  }
}
