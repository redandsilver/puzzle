package com.example.puzzle.domain.model.entity;

import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public void addRole(String role){
        roles.add(role);
    }
    public boolean verified(){
        return isVerify;
    }
}
