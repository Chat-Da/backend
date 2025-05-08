package site.chatda.domain.counsel.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeacherFeedbackDto {

    private String comment;

    private String jobSuggestion;

    private List<TeacherGuidanceDto> guidance;
}
