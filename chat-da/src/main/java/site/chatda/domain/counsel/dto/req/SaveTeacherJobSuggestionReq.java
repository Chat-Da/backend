package site.chatda.domain.counsel.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import site.chatda.domain.counsel.entity.Report;
import site.chatda.domain.counsel.entity.TeacherJobSuggestion;

@Data
public class SaveTeacherJobSuggestionReq {

    @NotBlank
    @Size(max = 500)
    private String content;

    public TeacherJobSuggestion toTeacherJobSuggestion(Report report) {

        return TeacherJobSuggestion.builder()
                .report(report)
                .content(content)
                .build();
    }
}
