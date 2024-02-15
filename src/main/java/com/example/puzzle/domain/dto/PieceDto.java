package com.example.puzzle.domain.dto;

import com.example.puzzle.domain.model.entity.Piece;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PieceDto {
    private String title;
    private String writerName;

    public static PieceDto from (Piece piece){
        return new PieceDto(
                piece.getTitle(),
                piece.getMember().getNickname());
    }
}
