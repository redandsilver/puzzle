package com.example.puzzle.service;

import com.example.puzzle.alarm.AlarmService;
import com.example.puzzle.alarm.message.Alarm;
import com.example.puzzle.domain.dto.PieceDto;
import com.example.puzzle.domain.model.entity.ImageUrl;
import com.example.puzzle.domain.model.entity.Member;
import com.example.puzzle.domain.model.entity.Piece;
import com.example.puzzle.domain.model.entity.form.PieceForm;
import com.example.puzzle.domain.repository.ImageUrlRepository;
import com.example.puzzle.domain.repository.MemberRepository;
import com.example.puzzle.domain.repository.PieceRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Slf4j
public class PieceService {

  private final PieceRepository pieceRepository;
  private final MemberRepository memberRepository;
  private final ImageUrlRepository imageUrlRepository;
  private final AlarmService alarmService;
  private final ImageService imageService;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Transactional
  public PieceDto createPiece(String name, PieceForm form) {
    Member member = memberRepository.findByNickname(name).orElseThrow(
        () -> new CustomException(ErrorCode.USER_NOT_FOUND)
    );
    Piece piece = pieceRepository.save(Piece.from(form));
    piece.writtenBy(member);
    return PieceDto.from(piece);
  }

  @Transactional
  public void uploadImages(Long pieceId, List<MultipartFile> multipartFiles) {
    Piece piece = pieceRepository.findById(pieceId).orElseThrow(
        () -> new CustomException(ErrorCode.PIECE_NOT_EXIST)
    );
    multipartFiles.forEach(multipartFile -> {
      String fileName = imageService.makefileName(multipartFile.getOriginalFilename());
      String awsUrl = imageService.uploadImage(multipartFile, fileName);
      ImageUrl imageUrl = ImageUrl.from(fileName, awsUrl);
      imageUrl.addImageToPiece(piece);
      imageUrlRepository.save(imageUrl);
    });
  }

  @Transactional
  public void deleteImage(Long pieceId, String fileName) {
    Piece piece = pieceRepository.findById(pieceId).orElseThrow(
        () -> new CustomException(ErrorCode.PIECE_NOT_EXIST)
    );

    imageService.deleteImageObject(fileName);
    imageUrlRepository.deleteByFileName(fileName);
  }


  @Transactional
  public PieceDto editPiece(Long id, PieceForm form) {
    Piece piece = pieceRepository.findById(id).orElseThrow(
        () -> new CustomException(ErrorCode.PIECE_NOT_EXIST)
    );
    piece.update(form);
    return PieceDto.from(piece);
  }

  public void deletePiece(Long id) {
    pieceRepository.deleteById(id);
  }

  @Transactional
  public void takePiece(String name, Long pieceId) {
    Member member = memberRepository.findByNickname(name).orElseThrow(
        () -> new CustomException(ErrorCode.USER_NOT_FOUND)
    );
    Piece piece = pieceRepository.findById(pieceId).orElseThrow(
        () -> new CustomException(ErrorCode.PIECE_NOT_EXIST)
    );
    if (!piece.isSecret() || !member.getNickname().equals(piece.getMember().getNickname())) {
      member.addPieceAuthority(pieceId);
    }
    alarmService.sendPieceAlarm(piece.getMember().getNickname(),
        Alarm.PIECE_TAKE_ALARM.formatMessage(name));
  }


}
