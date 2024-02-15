package com.example.puzzle.service;

import com.example.puzzle.domain.dto.CommentDto;
import com.example.puzzle.domain.model.entity.Comment;
import com.example.puzzle.domain.model.entity.Piece;
import com.example.puzzle.domain.model.entity.form.CommentForm;
import com.example.puzzle.domain.repository.CommentRepository;
import com.example.puzzle.domain.repository.PieceRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PieceRepository pieceRepository;


    public CommentDto createComment(String name, CommentForm form) {
        Piece piece = pieceRepository.findById(form.getPieceId()).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_EXIST)
        );
       Comment comment = commentRepository.save(Comment.from(form));
       comment.commentOn(piece);
       comment.commentBy(name);

        return CommentDto.from(comment);
    }

    public void editComment(Long commentId, CommentForm form) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_EXIST)
        );
        comment.update(form);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
