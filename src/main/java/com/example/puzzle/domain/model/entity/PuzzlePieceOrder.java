package com.example.puzzle.domain.model.entity;

import com.example.puzzle.domain.model.entity.form.PuzzlePieceOrderForm;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
@Table(
    name = "puzzle_piece_order",
    uniqueConstraints = @UniqueConstraint(columnNames = {"puzzle_id", "order_index"})
)
public class PuzzlePieceOrder extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "puzzle_id")
  private Puzzle puzzle;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "piece_id")
  private Piece piece;

  @Column(name = "order_index")
  private int orderIndex;

  public void makePuzzle(Puzzle puzzle, Piece piece) {
    this.setPuzzle(puzzle);
    this.setPiece(piece);
    puzzle.addPiece(this);
    piece.addPuzzle(this);
  }

  public void update(Puzzle puzzle, Piece piece, PuzzlePieceOrderForm form) {
    this.setPuzzle(puzzle);
    this.setPiece(piece);
    this.setOrderIndex(form.getIndexOrder());
  }
}
