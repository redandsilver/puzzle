package com.example.puzzle.domain.model.entity.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PieceForm {

  private String title;
  private String content;
  private boolean isSecret;
}
