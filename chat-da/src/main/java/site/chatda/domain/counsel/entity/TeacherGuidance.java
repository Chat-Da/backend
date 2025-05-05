package site.chatda.domain.counsel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.chatda.domain.counsel.entity.id.TeacherGuidanceId;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherGuidance {

    @EmbeddedId
    private TeacherGuidanceId id;

    @ManyToOne
    @MapsId("counselId")
    @JoinColumn(name = "counsel_id", columnDefinition = "INT UNSIGNED")
    private Report report;

    @NotNull
    @Column(length = 200)
    private String content;
}
