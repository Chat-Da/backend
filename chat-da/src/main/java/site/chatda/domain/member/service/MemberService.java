package site.chatda.domain.member.service;

import site.chatda.domain.member.dto.res.MemberDetailsRes;
import site.chatda.domain.member.entity.Member;

public interface MemberService {

    MemberDetailsRes findMemberDetails(Member member);
}
