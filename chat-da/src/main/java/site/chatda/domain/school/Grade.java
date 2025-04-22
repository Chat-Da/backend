package site.chatda.domain.school;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @EmbeddedId
    private GradeId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("schoolId")
    @JoinColumn(name = "school_id", columnDefinition = "SMALLINT", nullable = false)
    private School school;
}
