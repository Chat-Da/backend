package site.chatda.domain.counsel.service;

import site.chatda.domain.counsel.dto.req.ChangeStepReq;
import site.chatda.domain.counsel.dto.req.CreateReportReq;
import site.chatda.domain.counsel.dto.res.CounselListRes;
import site.chatda.domain.member.entity.Member;

public interface CounselService {

    void applyCounsel(Member member);

    void openCounsel(Member member, Long studentId);

    CounselListRes findStudentCounsels(Member member, Long studentId);

    CounselListRes findCounsels(Long studentId);

    void changeCounselStep(Member member, Long counselId, ChangeStepReq changeStepReq);

    void createReport(Long counselId, CreateReportReq createReportReq);
}
