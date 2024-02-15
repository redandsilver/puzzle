package com.example.puzzle.service;

import com.example.puzzle.domain.model.entity.Comment;
import com.example.puzzle.domain.model.entity.Piece;
import com.example.puzzle.domain.repository.CommentRepository;
import com.example.puzzle.domain.repository.PieceRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class FilterService {
    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final PieceRepository pieceRepository;
    private final CommentRepository commentRepository;

    public String resolveTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader(TOKEN_HEADER);
        if(!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)){
            return token.substring(TOKEN_PREFIX.length()); // 실제 토큰 부위
        }
        return null;
    }

    public boolean isPieceWriter(String name, Long pieceId){
        Piece piece = pieceRepository.findById(pieceId).orElseThrow(
                () -> new CustomException(ErrorCode.PIECE_NOT_EXIST)
        );
        return name.equals(piece.getMember().getNickname());
    }
    public boolean isCommentWriterOrPieceWriter(String name, String method, Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_EXIST)
        );
        if(method.equals("DELETE")){
            Piece piece = pieceRepository.findById(comment.getPiece().getId()).orElseThrow(
                    () -> new CustomException(ErrorCode.PIECE_NOT_EXIST)
            );
            return name.equals(piece.getMember().getNickname());
        }else{
            return name.equals(comment.getWritername());
        }
    }
}
