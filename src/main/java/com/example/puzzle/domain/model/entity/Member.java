package com.example.puzzle.domain.model.entity;

import lombok.*;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;
import java.time.LocalDateTime;

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
}
