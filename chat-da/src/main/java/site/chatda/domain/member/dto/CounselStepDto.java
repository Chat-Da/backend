package site.chatda.domain.member.dto;

import lombok.Data;
import site.chatda.domain.counsel.enums.CounselStep;

@Data
public class CounselStepDto {

    private Long studentId;

    private CounselStep step;

    public CounselStepDto(Long studentId, CounselStep step) {
        this.studentId = studentId;
        this.step = step;
    }
}
