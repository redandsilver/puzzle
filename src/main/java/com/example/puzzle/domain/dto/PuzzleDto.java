package com.example.puzzle.domain.dto;

import com.example.puzzle.domain.model.entity.Puzzle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PuzzleDto {

  private String puzzleName;
  private String wirtername;

  public static PuzzleDto from(Puzzle puzzle) {
    return new PuzzleDto(
        puzzle.getPuzzleName(),
        puzzle.getWriterName()
    );
  }

}
