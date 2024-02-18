package com.example.puzzle.alarm;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class AlarmController {

  private final AlarmService alarmService;

  // sse 구독 연결
  @GetMapping(value = "/subscribe", produces = "text/event-stream")
  public SseEmitter subscribe(Principal principal) {
    return alarmService.connectAlarm(principal.getName());
  }
}
