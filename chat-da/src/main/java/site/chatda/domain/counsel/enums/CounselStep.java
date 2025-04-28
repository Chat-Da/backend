package site.chatda.domain.counsel.enums;

import lombok.Getter;

@Getter
public enum CounselStep {
    NOTHING("상담 없음"),
    REQUESTED("상담 신청중"),
    PENDING("상담 대기중"),
    IN_PROGRESS("상담 진행중"),
    RESULT_WAITING("결과 대기중"),
    COMPLETED("상담 완료")
    ;

    private final String description;

    CounselStep(String description) {
        this.description = description;
    }
}
