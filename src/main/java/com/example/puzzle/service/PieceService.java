package com.example.puzzle.service;

import com.example.puzzle.domain.dto.PieceDto;
import com.example.puzzle.domain.model.entity.Member;
import com.example.puzzle.domain.model.entity.Piece;
import com.example.puzzle.domain.model.entity.form.PieceForm;
import com.example.puzzle.domain.repository.MemberRepository;
import com.example.puzzle.domain.repository.PieceRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PieceService {

  private final PieceRepository pieceRepository;
  private final MemberRepository memberRepository;

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
    if (!piece.isSecret() && !member.getNickname().equals(piece.getMember().getNickname())) {
      member.addPieceAuthority(pieceId);
    }
  }
}
