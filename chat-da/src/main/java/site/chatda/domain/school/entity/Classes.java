package site.chatda.domain.school.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.chatda.domain.school.entity.id.ClassesId;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Classes {

    @EmbeddedId
    private ClassesId id;

    @ManyToOne(fetch = LAZY, optional = false)
    @MapsId("schoolId")
    @JoinColumn(name = "school_id", columnDefinition = "SMALLINT", nullable = false)
    private School school;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "school_id", referencedColumnName = "school_id", insertable = false, updatable = false),
            @JoinColumn(name = "level", referencedColumnName = "level", insertable = false, updatable = false)
    })
    private Grade grade;
}
