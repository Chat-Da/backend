package site.chatda.domain.counsel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.chatda.domain.counsel.entity.id.JobRecommendationId;
import site.chatda.domain.job.entity.Job;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRecommendation {

    @EmbeddedId
    private JobRecommendationId id;

    @ManyToOne(fetch = LAZY, optional = false)
    @MapsId("jobId")
    @JoinColumn(name = "job_id", columnDefinition = "SMALLINT")
    private Job job;

    @ManyToOne(fetch = LAZY, optional = false)
    @MapsId("counselId")
    @JoinColumn(name = "counsel_id", columnDefinition = "INT UNSIGNED")
    private Report report;

    @NotBlank
    @Column(length = 300)
    private String reason;
}
