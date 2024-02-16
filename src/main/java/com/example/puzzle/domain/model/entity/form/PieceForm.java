package com.example.puzzle.domain.model.entity.form;

import com.example.puzzle.domain.model.entity.Piece;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PieceForm{
    private String title;
    private String content;
    private boolean isSecret;

}
