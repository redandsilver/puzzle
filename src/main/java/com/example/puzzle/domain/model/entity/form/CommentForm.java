package com.example.puzzle.domain.model.entity.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;



@Getter
@Builder
@AllArgsConstructor
public class CommentForm {
    private Long parentId;
    private String content;
    private Long pieceId;
}
