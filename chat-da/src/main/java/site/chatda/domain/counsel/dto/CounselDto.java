package site.chatda.domain.counsel.dto;

import lombok.Data;
import site.chatda.domain.counsel.entity.Counsel;
import site.chatda.global.util.DateFormatter;

import static site.chatda.domain.counsel.enums.CounselStep.COMPLETED;

@Data
public class CounselDto {

    private Long counselId;

    private String teacherName;

    private String step;

    private String startDate;

    private String endDate;

    public CounselDto(Counsel counsel) {
        this.counselId = counsel.getId();
        this.teacherName = counsel.getTeacher().getMember().getName();
        this.step = counsel.getStep().getDescription();
        this.startDate = DateFormatter.convertToString(counsel.getCreatedAt());
        this.endDate = counsel.getStep() == COMPLETED ? DateFormatter.convertToString(counsel.getModifiedAt()) : null;
    }
}
