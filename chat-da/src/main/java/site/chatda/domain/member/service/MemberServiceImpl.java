package site.chatda.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chatda.domain.member.dto.res.MemberDetailsRes;
import site.chatda.domain.member.entity.Member;
import site.chatda.domain.school.entity.School;
import site.chatda.domain.school.repository.SchoolRepository;
import site.chatda.global.exception.CustomException;

import static site.chatda.global.statuscode.ErrorCode.NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final SchoolRepository schoolRepository;

    @Override
    public MemberDetailsRes findMemberDetails(Member member) {

        Integer schoolId = member.getClasses().getId().getSchoolId();

        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        return new MemberDetailsRes(school, member);
    }
}
