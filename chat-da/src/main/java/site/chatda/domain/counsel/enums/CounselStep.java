package site.chatda.domain.counsel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.chatda.global.exception.CustomException;

import static site.chatda.global.statuscode.ErrorCode.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum CounselStep {
    REQUESTED("상담 신청중"),
    PENDING("상담 대기중"),
    IN_PROGRESS("상담 진행중"),
    RESULT_WAITING("결과 대기중"),
    COMPLETED("상담 완료")
    ;

    private final String description;

    public static CounselStep getByDescription(String description) {

        for (CounselStep step : CounselStep.values()) {
            if (step.getDescription().equals(description)) {
                return step;
            }
        }

        throw new CustomException(BAD_REQUEST);
    }
}
