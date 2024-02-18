package com.example.puzzle.service;

import com.example.puzzle.alarm.AlarmService;
import com.example.puzzle.alarm.message.Alarm;
import com.example.puzzle.domain.dto.CommentDto;
import com.example.puzzle.domain.model.entity.Comment;
import com.example.puzzle.domain.model.entity.Piece;
import com.example.puzzle.domain.model.entity.form.CommentForm;
import com.example.puzzle.domain.repository.CommentRepository;
import com.example.puzzle.domain.repository.PieceRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

  private final CommentRepository commentRepository;
  private final PieceRepository pieceRepository;
  private final AlarmService alarmService;

  @Transactional
  public CommentDto createComment(String name, CommentForm form) {
    Piece piece = pieceRepository.findById(form.getPieceId()).orElseThrow(
        () -> new CustomException(ErrorCode.PIECE_NOT_EXIST)
    );
    Comment parentComment = null;
    if (form.getParentId() != 0) {
      parentComment = commentRepository.findById(form.getParentId()).orElseThrow(
          () -> new CustomException(ErrorCode.COMMENT_NOT_EXIST)
      );
    }
    Comment comment = new Comment();
    comment.commentOn(piece);
    comment.commentBy(name);
    commentRepository.save(Comment.from(form));

    String writerName = piece.getMember().getNickname();
    if (parentComment == null) {
      alarmService.sendCommentAlarm(name, writerName, Alarm.COMMENT_ALARM.formatMessage(name));
    } else {
      if (!parentComment.getWritername().equals(name)) {
        alarmService.sendCommentAlarm(name, writerName, Alarm.COMMENT_ALARM.formatMessage(name));
      }
    }

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
