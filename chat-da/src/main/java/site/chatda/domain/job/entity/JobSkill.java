package site.chatda.domain.job.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSkill {

    @EmbeddedId
    private JobSkillId id;

    @ManyToOne(fetch = LAZY, optional = false)
    @MapsId("jobId")
    @JoinColumn(name = "job_id", columnDefinition = "SMALLINT")
    private Job job;

    @ManyToOne(fetch = LAZY, optional = false)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id", columnDefinition = "MEDIUMINT")
    private Skill skill;
}
