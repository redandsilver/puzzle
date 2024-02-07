package com.example.puzzle.domain.model.entity;

import com.example.puzzle.domain.model.entity.form.PieceForm;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Piece extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "piece_id", nullable = false)
    private Long id;
    @Size(min = 1, max = 15)
    private String title;
    @Size(min = 1, max = 500)
    private String content;
    private String writerName;
    private boolean isSecret;
    public boolean isSecret(){
        return isSecret;
    }

    public void update(PieceForm form) {
        this.title = form.getTitle();
        this.content = form.getContent();
        this.isSecret = form.isSecret();
    }
}
