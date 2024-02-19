package com.example.puzzle.domain.model.entity.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageUrlForm {

  private String fileName;
  private String url;
}
