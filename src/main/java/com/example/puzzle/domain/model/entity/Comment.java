package com.example.puzzle.domain.model.entity;

import com.example.puzzle.domain.model.entity.form.CommentForm;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
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
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id", nullable = false)
  private Long id;
  private Long parentId;
  @Size(min = 1, max = 50)
  private String content;
  private String writername;

  @ManyToOne
  @JoinColumn(name = "piece_id")
  private Piece piece;

  public static Comment from(CommentForm form) {
    return Comment.builder()
        .content(form.getContent())
        .parentId(form.getParentId())
        .build();
  }

  public void commentOn(Piece piece) {
    this.setPiece(piece);
    piece.addComment(this);
  }

  public void commentBy(String name) {
    this.writername = name;
  }

  public void update(CommentForm form) {
    this.content = form.getContent();
  }
}
