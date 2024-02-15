package com.example.puzzle.domain.model.entity;

import com.example.puzzle.domain.model.entity.form.PieceForm;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;
    private boolean isSecret;

    @OneToMany(mappedBy = "piece")
    List<Comment> replies;
    public static Piece from (PieceForm form){
        return Piece.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .isSecret(form.isSecret())
                .build();
    }

    public void addReply(Comment comment){
        if(replies == null) replies = new ArrayList<>();
        replies.add(comment);
    }

    public boolean isSecret(){
        return isSecret;
    }

    public void update(PieceForm form) {
        this.title = form.getTitle();
        this.content = form.getContent();
        this.isSecret = form.isSecret();
    }
    public void writtenBy (Member member){
        this.setMember(member);
        member.addPiece(this);
    }
}
