package site.chatda.domain.counsel.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.chatda.domain.counsel.dto.req.ChangeStepReq;
import site.chatda.domain.counsel.dto.req.CreateReportReq;
import site.chatda.domain.counsel.dto.res.CounselListRes;
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
                                        @RequestBody ChangeStepReq changeStepReq) {

        counselService.changeCounselStep(member, counselId, changeStepReq);

        return ResponseDto.success(OK);
    }

    @PostMapping("{counselId}/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto<Void> createReport(@PathVariable("counselId") Long counselId,
                                          @RequestBody @Valid CreateReportReq createReportReq) {

        counselService.createReport(counselId, createReportReq);

        return ResponseDto.success(CREATED);
    }
}
