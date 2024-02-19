package com.example.puzzle.domain.model.entity;

import com.example.puzzle.domain.model.entity.form.PieceForm;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class Piece extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "piece_id", nullable = false)
  private Long id;
  @Size(min = 1, max = 15)
  private String title;
  @Size(min = 1, max = 500)
  private String content;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;
  private boolean isSecret;

  @OneToMany(mappedBy = "piece", cascade = CascadeType.REMOVE)
  List<Comment> comments;

  @OneToMany(mappedBy = "piece", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<PuzzlePieceOrder> puzzlePieceOrders;
  @OneToMany(mappedBy = "piece", cascade = CascadeType.REMOVE)
  private List<ImageUrl> imageUrls;

  public static Piece from(PieceForm form) {
    return Piece.builder()
        .title(form.getTitle())
        .content(form.getContent())
        .isSecret(form.isSecret())
        .build();
  }

  public void addImageUrl(ImageUrl url) {
    if (imageUrls == null) {
      imageUrls = new ArrayList<>();
    }
    imageUrls.add(url);
  }

  public void addComment(Comment comment) {
    if (comments == null) {
      comments = new ArrayList<>();
    }
    comments.add(comment);
  }

  public void addPuzzle(PuzzlePieceOrder puzzlePieceOrder) {
    if (puzzlePieceOrders == null) {
      puzzlePieceOrders = new ArrayList<>();
    }
    puzzlePieceOrders.add(puzzlePieceOrder);
  }

  public boolean isSecret() {
    return isSecret;
  }

  public void update(PieceForm form) {
    this.title = form.getTitle();
    this.content = form.getContent();
    this.isSecret = form.isSecret();
  }

  public void writtenBy(Member member) {
    this.setMember(member);
    member.addPiece(this);
  }

}
