package site.chatda.domain.counsel.dto.req;

import jakarta.validation.constraints.*;
import lombok.Data;
import site.chatda.domain.counsel.entity.Report;
import site.chatda.domain.counsel.entity.TeacherComment;

@Data
public class SaveTeacherCommentReq {

    @NotBlank
    @Size(max = 500)
    private String content;

    public TeacherComment toTeacherComment(Report report) {

        return TeacherComment.builder()
                .report(report)
                .content(content)
                .build();
    }
}
