package site.chatda.domain.counsel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherComment {

    @Id
    @Column(name = "counsel_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "counsel_id", columnDefinition = "INT UNSIGNED")
    private Report report;

    @NotBlank
    @Column(length = 500)
    private String content;

    public void updateContent(String content) {
        this.content = content;
    }
}
