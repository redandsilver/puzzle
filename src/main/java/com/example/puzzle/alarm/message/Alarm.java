package com.example.puzzle.alarm.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Alarm {
  PIECE_TAKE_ALARM("%s님이 조각을 떼어갔어요"),
  COMMENT_ALARM("%s님이 조각에 댓글을 달았어요"),
  REPLY_ALARM("%s님이 조각에 답글을 달았어요");

  private final String message;

  public String formatMessage(String username) {
    return String.format("%s%s", username, message);
  }
}
