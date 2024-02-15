package com.example.puzzle.controller;

import com.example.puzzle.domain.dto.CommentDto;
import com.example.puzzle.domain.model.entity.form.CommentForm;
import com.example.puzzle.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/create")
    public ResponseEntity<String> createComment (
            Principal principal, CommentForm form){
        CommentDto commentDto = commentService.createComment(principal.getName(),form);
        return ResponseEntity.ok(commentDto.getWriterName()+" 님이 댓글을 남겼습니다.");
    }
    @PutMapping("/edit/{comment_id}")
    public ResponseEntity<String> editComment  (
            @PathVariable Long commentId, @RequestBody CommentForm form){
        commentService.editComment(commentId,form);
        return ResponseEntity.ok("수정되었습니다.");
    }

    @DeleteMapping("/delete/{comment_id}")
    public ResponseEntity<String> deleteComment (
           @PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("삭제되었습니다.");
    }
}
