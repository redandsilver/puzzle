package com.example.puzzle.alarm;

import com.example.puzzle.alarm.message.Alarm;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

  private final EmitterRepository emitterRepository;
  private final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
  private final String ALARM_NAME = "SSE";

  public void sendPieceAlarm(String writername, String msg) {
    emitterRepository.get(writername).ifPresentOrElse(sseEmitter -> {
      try {
        sseEmitter.send(
            SseEmitter.event().id(createAlarmId(writername)).name(ALARM_NAME).data(msg));
      } catch (IOException e) {
        emitterRepository.delete(writername);
        throw new CustomException(ErrorCode.SSE_ERROR);
      }
    }, () -> log.info("[SseEmitter] {} SseEmitter Not Founded", writername));
  }

  public void sendCommentAlarm(String username, String writername, String msg) {
    emitterRepository.get(writername).ifPresentOrElse(sseEmitter -> {
      try {
        sseEmitter.send(
            SseEmitter.event().id(createAlarmId(writername)).name(ALARM_NAME).data(
                Alarm.COMMENT_ALARM.formatMessage(username)));
      } catch (IOException e) {
        emitterRepository.delete(writername);
        throw new CustomException(ErrorCode.SSE_ERROR);
      }
    }, () -> log.info("[SseEmitter] {} SseEmitter Not Founded", writername));
  }


  public SseEmitter connectAlarm(String username) {
    SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
    emitterRepository.save(username, sseEmitter);

    // 종료 되었을 때 처리
    sseEmitter.onCompletion(() -> {
      emitterRepository.delete(username);
    });

    // timeOut 시 처리
    sseEmitter.onTimeout(() -> {
      emitterRepository.delete(username);
    });

    try {
      sseEmitter.send(SseEmitter.event().id(createAlarmId(username)).name(ALARM_NAME)
          .data("connect completed!!"));
    } catch (IOException e) {
      throw new CustomException(ErrorCode.SERVER_DISCONNECT);
    }

    return sseEmitter;
  }

  private String createAlarmId(String username) {

    return username + "_" + System.currentTimeMillis();
  }
}
