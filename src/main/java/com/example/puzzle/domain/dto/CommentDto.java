package com.example.puzzle.domain.dto;

import com.example.puzzle.domain.model.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long pieceId;
    private String writerName;
    private String content;
    public static CommentDto from (Comment comment){
        return new CommentDto(
                comment.getId(),
                comment.getPiece().getId(),
                comment.getWritername(),
                comment.getContent());
    }
}