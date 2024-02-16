package com.example.puzzle.domain.model.entity.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentForm {
    private Long parentId;
    private String content;
    private Long pieceId;
}
