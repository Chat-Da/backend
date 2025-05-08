package site.chatda.domain.counsel.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.chatda.domain.counsel.dto.req.*;
import site.chatda.domain.counsel.dto.res.CounselListRes;
import site.chatda.domain.counsel.dto.res.ReportDetailsRes;
import site.chatda.domain.counsel.service.CounselService;
import site.chatda.domain.member.entity.Member;
import site.chatda.global.argument_resolver.LoginMember;
import site.chatda.global.dto.ResponseDto;

import static site.chatda.global.statuscode.SuccessCode.CREATED;
import static site.chatda.global.statuscode.SuccessCode.OK;

@RestController
@RequestMapping("/api/counsels")
@RequiredArgsConstructor
public class CounselController {

    private final CounselService counselService;

    @PostMapping("/students")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseDto<Void> counselApplication(@LoginMember Member member) {

        counselService.applyCounsel(member);

        return ResponseDto.success(CREATED);
    }

    @PostMapping("/students/{studentId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseDto<Void> openCounsel(@LoginMember Member member,
                                         @PathVariable("studentId") Long studentId) {

        counselService.openCounsel(member, studentId);

        return ResponseDto.success(CREATED);
    }

    @GetMapping("/students/{studentId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseDto<CounselListRes> counselList(@LoginMember Member member,
                                                   @PathVariable("studentId") Long studentId) {

        CounselListRes result = counselService.findStudentCounsels(member, studentId);

        return ResponseDto.success(OK, result);
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseDto<CounselListRes> counselList(@LoginMember Member member) {

        CounselListRes result = counselService.findCounsels(member.getId());

        return ResponseDto.success(OK, result);
    }

    @PatchMapping("/{counselId}")
    public ResponseDto<Void> changeStep(@LoginMember Member member,
                                        @PathVariable("counselId") Long counselId,
                                        @Valid @RequestBody ChangeStepReq changeStepReq) {

        counselService.changeCounselStep(member, counselId, changeStepReq);

        return ResponseDto.success(OK);
    }

    @PostMapping("{counselId}/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto<Void> createReport(@PathVariable("counselId") Long counselId,
                                          @Valid @RequestBody CreateReportReq createReportReq) {

        counselService.createReport(counselId, createReportReq);

        return ResponseDto.success(CREATED);
    }

    @PutMapping("/{counselId}/comments")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseDto<Void> saveTeacherComment(@LoginMember Member member,
                                                @PathVariable("counselId") Long counselId,
                                                @Valid @RequestBody SaveTeacherCommentReq saveTeacherCommentReq) {

        counselService.saveTeacherComment(member, counselId, saveTeacherCommentReq);

        return ResponseDto.success(CREATED);
    }

    @PutMapping("/{counselId}/job_suggestions")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseDto<Void> saveTeacherJobSuggestion(@LoginMember Member member,
                                                @PathVariable("counselId") Long counselId,
                                                @Valid @RequestBody SaveTeacherJobSuggestionReq saveTeacherJobSuggestionReq) {

        counselService.saveTeacherJobSuggestion(member, counselId, saveTeacherJobSuggestionReq);

        return ResponseDto.success(CREATED);
    }

    @PutMapping("/{counselId}/guidance/{seq}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseDto<Void> saveTeacherGuidance(@LoginMember Member member,
                                                 @PathVariable("counselId") Long counselId,
                                                 @PathVariable("seq") Integer seq,
                                                 @Valid @RequestBody SaveTeacherGuidanceReq saveTeacherGuidanceReq) {

        counselService.saveTeacherGuidance(member, counselId, seq, saveTeacherGuidanceReq);

        return ResponseDto.success(CREATED);
    }

    @GetMapping("/{counselId}/reports")
    public ResponseDto<ReportDetailsRes> reportDetails(@LoginMember Member member,
                                                       @PathVariable("counselId") Long counselId) {

        ReportDetailsRes result = counselService.getReportDetails(member, counselId);

        return ResponseDto.success(OK, result);
    }
}
