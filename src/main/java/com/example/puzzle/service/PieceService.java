package com.example.puzzle.service;

import com.example.puzzle.domain.dto.PieceDto;
import com.example.puzzle.domain.model.entity.form.PieceForm;
import com.example.puzzle.domain.model.entity.Piece;
import com.example.puzzle.domain.repository.PieceRepository;
import com.example.puzzle.exception.CustomException;
import com.example.puzzle.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class PieceService {
    private final PieceRepository pieceRepository;
    public PieceDto createPiece(String writerName, PieceForm form) {
        Piece piece = pieceRepository.save(
                Piece.builder()
                        .title(form.getTitle())
                        .content(form.getContent())
                        .writerName(writerName)
                        .isSecret(form.isSecret())
                        .build());
        return PieceDto.from(piece);
    }
    @Transactional
    public PieceDto editPiece(Long id, PieceForm form) {
        Piece piece = pieceRepository.findById(id).orElseThrow(
                ()-> new CustomException(ErrorCode.PIECE_NOT_EXIST)
        );
        piece.update(form);
        return PieceDto.from(piece);
    }



    public void deletePiece(Long id) {
        pieceRepository.deleteById(id);
    }
}
