package site.chatda.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.chatda.domain.member.dto.res.MemberDetailsRes;
import site.chatda.domain.member.entity.Member;
import site.chatda.domain.member.service.MemberService;
import site.chatda.global.argument_resolver.LoginMember;
import site.chatda.global.dto.ResponseDto;

import static site.chatda.global.statuscode.SuccessCode.OK;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseDto<MemberDetailsRes> memberDetails(@LoginMember Member member) {
        MemberDetailsRes result = memberService.findMemberDetails(member);

        return ResponseDto.success(OK, result);
    }
}
