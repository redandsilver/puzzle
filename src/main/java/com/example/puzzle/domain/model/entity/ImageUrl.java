package com.example.puzzle.domain.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class ImageUrl extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "url_id", nullable = false)
  private Long id;
  private String url;
  private String fileName;
  @ManyToOne
  @JoinColumn(name = "piece_id")
  private Piece piece;

  public static ImageUrl from(String fileName, String url) {
    return ImageUrl.builder()
        .fileName(fileName)
        .url(url)
        .build();
  }

  public void addImageToPiece(Piece piece) {
    this.setPiece(piece);
    piece.addImageUrl(this);
  }
}
