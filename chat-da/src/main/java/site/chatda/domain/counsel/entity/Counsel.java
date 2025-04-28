package site.chatda.domain.counsel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import site.chatda.domain.counsel.enums.CounselStep;
import site.chatda.domain.member.entity.Student;
import site.chatda.domain.member.entity.Teacher;
import site.chatda.domain.school.entity.Grade;
import site.chatda.global.entity.BaseEntity;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = " UPDATE Counsel SET is_deleted = true WHERE counsel_id = ? ")
@SQLRestriction("is_deleted = false")
public class Counsel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "counsel_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "school_id", referencedColumnName = "school_id"),
            @JoinColumn(name = "level", referencedColumnName = "level")
    })
    private Grade grade;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @NotNull
    @Enumerated(STRING)
    private CounselStep step;
}
