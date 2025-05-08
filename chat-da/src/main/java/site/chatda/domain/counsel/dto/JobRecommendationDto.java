package site.chatda.domain.counsel.dto;

import lombok.Data;
import site.chatda.domain.counsel.entity.JobRecommendation;
import site.chatda.domain.job.entity.JobSkill;

import java.util.List;

@Data
public class JobRecommendationDto {

    private Integer id;

    private String name;

    private String recommendReason;

    private String growthSuggestion;

    private List<String> requiredSkills;

    public JobRecommendationDto(JobRecommendation jobRecommendation, List<JobSkill> jobSkills) {
        this.id = jobRecommendation.getJob().getId();
        this.name = jobRecommendation.getJob().getName();
        this.recommendReason = jobRecommendation.getReason();
        this.growthSuggestion = jobRecommendation.getGrowthSuggestions();

        this.requiredSkills = jobSkills.stream()
                .map(jobSkill -> jobSkill.getSkill().getName())
                .toList();
    }
}
