package com.example.puzzle.domain.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id", nullable = false)
  private Long id;
  private String phoneNumber;
  private String nickname;
  private String password;
  private String profileImageUrl;
  private String verificationCode;
  private LocalDateTime verifyExpiredAt;
  private boolean isVerify;
  @OneToMany(mappedBy = "member")
  private List<Piece> pieces;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> roles;
  @ElementCollection(fetch = FetchType.LAZY)
  private List<Long> pieceAuthorites;

  public void addRole(String role) {
    roles.add(role);
  }

  public void addPieceAuthority(Long pieceId) {
    if (pieceAuthorites == null) {
      pieces = new ArrayList<>();
    }
    pieceAuthorites.add(pieceId);
  }

  public void addPiece(Piece piece) {
    if (pieces == null) {
      pieces = new ArrayList<>();
    }
    pieces.add(piece);
  }

  public boolean verified() {
    return isVerify;
  }
}
