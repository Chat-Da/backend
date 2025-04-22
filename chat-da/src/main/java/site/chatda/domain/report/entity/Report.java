package site.chatda.domain.report.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import site.chatda.domain.member.entity.Student;
import site.chatda.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = " UPDATE Member SET is_deleted = true WHERE report_id = ? ")
@SQLRestriction("is_deleted = false")
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Student student;
}
