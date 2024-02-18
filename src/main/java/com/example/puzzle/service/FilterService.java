package com.example.puzzle.service;

import com.example.puzzle.domain.model.entity.Comment;
import com.example.puzzle.domain.model.entity.Piece;
import com.example.puzzle.domain.model.entity.Puzzle;
import com.example.puzzle.domain.repository.CommentRepository;
import com.example.puzzle.domain.repository.PieceRepository;
import com.example.puzzle.domain.repository.PuzzleRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


@Service
@RequiredArgsConstructor
@Slf4j
public class FilterService {

  private static final String TOKEN_HEADER = "Authorization";
  private static final String TOKEN_PREFIX = "Bearer ";
  private final PieceRepository pieceRepository;
  private final CommentRepository commentRepository;
  private final PuzzleRepository puzzleRepository;

  public String resolveTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader(TOKEN_HEADER);
    if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
      return token.substring(TOKEN_PREFIX.length()); // 실제 토큰 부위
    }
    return null;
  }

  public void validatePiecePermission(String name, Long pieceId) {
    Piece piece = pieceRepository.findById(pieceId).orElseThrow(
        () -> new CustomException(ErrorCode.PIECE_NOT_EXIST)
    );
    if (!isWriter(name, piece.getMember().getNickname())) {
      throw new CustomException(ErrorCode.WRONG_ACCESS);
    }
  }

  public void validatePuzzlePermission(String name, Long puzzleId) {
    Puzzle puzzle = puzzleRepository.findById(puzzleId).orElseThrow(
        () -> new CustomException(ErrorCode.PUZZLE_NOT_EXIST)
    );
    if (!isWriter(name, puzzle.getWriterName())) {
      throw new CustomException(ErrorCode.WRONG_ACCESS);
    }
  }

  public void validateCommentPermission(String name, String method, Long commentId) {
    Comment comment = commentRepository.findById(commentId).orElseThrow(
        () -> new CustomException(ErrorCode.COMMENT_NOT_EXIST)
    );
    if (method.equals("PUT")) {
      if (!isWriter(name, comment.getWritername())) {
        throw new CustomException(ErrorCode.WRONG_ACCESS);
      }
    } else {
      validateDeletePermission(name, comment);
    }

  }

  public Boolean isWriter(String name, String writerName) {
    return writerName.equals(name);
  }

  private void validateDeletePermission(String name, Comment comment) {
    if (!name.equals(comment.getWritername()) &&
        !name.equals(comment.getPiece().getMember().getNickname())) {
      throw new CustomException(ErrorCode.WRONG_ACCESS);
    }
  }
}
