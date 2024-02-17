package com.example.puzzle.domain.model.entity;

import com.example.puzzle.domain.model.entity.form.PuzzleForm;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class Puzzle extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "puzzle_id", nullable = false)
  private Long id;
  private String puzzleName;
  private String writerName;
  @OneToMany(mappedBy = "puzzle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<PuzzlePieceOrder> puzzlePieceOrders;

  public static Puzzle from(PuzzleForm form) {
    return com.example.puzzle.domain.model.entity.Puzzle.builder()
        .puzzleName(form.getPuzzleName())
        .build();
  }

  public void addPiece(PuzzlePieceOrder puzzlePieceOrder) {
    if (puzzlePieceOrders == null) {
      puzzlePieceOrders = new ArrayList<>();
    }
    puzzlePieceOrders.add(puzzlePieceOrder);
  }


  public void updatePuzzleName(String puzzleName) {
    this.puzzleName = puzzleName;

  }

}
