package com.example.puzzle.domain.model.entity.form;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PuzzleForm {

  private String puzzleName;
  private List<PuzzlePieceOrderForm> pieces;
}
