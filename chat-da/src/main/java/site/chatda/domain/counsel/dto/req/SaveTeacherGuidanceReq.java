package site.chatda.domain.counsel.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import site.chatda.domain.counsel.entity.Report;
import site.chatda.domain.counsel.entity.TeacherGuidance;
import site.chatda.domain.counsel.entity.id.TeacherGuidanceId;

@Data
public class SaveTeacherGuidanceReq {

    @NotBlank
    @Size(max = 500)
    private String content;

    public TeacherGuidance teacherGuidance(Report report, Integer seq) {

        return TeacherGuidance.builder()
                .id(new TeacherGuidanceId(report.getId(), seq))
                .report(report)
                .content(content)
                .build();
    }
}
