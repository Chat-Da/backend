package site.chatda.domain.counsel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.chatda.domain.counsel.service.CounselService;
import site.chatda.domain.member.entity.Member;
import site.chatda.global.argument_resolver.LoginMember;
import site.chatda.global.dto.ResponseDto;

import static site.chatda.global.statuscode.SuccessCode.CREATED;

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
}
