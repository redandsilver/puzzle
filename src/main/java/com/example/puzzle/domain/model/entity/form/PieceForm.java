package com.example.puzzle.domain.model.entity.form;

import com.example.puzzle.domain.model.entity.Piece;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PieceForm{
    private String title;
    private String content;
    private boolean isSecret;

    public PieceForm from (Piece piece){
        return new PieceForm(
                piece.getTitle(),
                piece.getContent(),
                piece.isSecret());
    }
}
