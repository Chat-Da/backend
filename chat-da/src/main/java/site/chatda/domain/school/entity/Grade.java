package site.chatda.domain.school.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.chatda.domain.school.entity.id.GradeId;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @EmbeddedId
    private GradeId id;

    @ManyToOne(fetch = LAZY, optional = false)
    @MapsId("schoolId")
    @JoinColumn(name = "school_id", columnDefinition = "SMALLINT", nullable = false)
    private School school;
}
