package com.example.puzzle.domain.model.entity.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PuzzlePieceOrderForm {

  private int indexOrder;
  private Long pieceId;
}
