package site.chatda.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chatda.domain.counsel.enums.CounselStep;
import site.chatda.domain.member.dto.CounselStepDto;
import site.chatda.domain.member.dto.StudentDto;
import site.chatda.domain.member.dto.res.MemberDetailsRes;
import site.chatda.domain.member.dto.res.StudentListRes;
import site.chatda.domain.member.entity.Member;
import site.chatda.domain.member.entity.Student;
import site.chatda.domain.member.repository.MemberRepository;
import site.chatda.domain.school.entity.School;
import site.chatda.domain.school.repository.SchoolRepository;
import site.chatda.global.exception.CustomException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static site.chatda.domain.counsel.enums.CounselStep.getByName;
import static site.chatda.global.statuscode.ErrorCode.NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final SchoolRepository schoolRepository;

    @Override
    public MemberDetailsRes findMemberDetails(Member member) {

        Integer schoolId = member.getClasses().getId().getSchoolId();

        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        return new MemberDetailsRes(school, member);
    }

    @Override
    public StudentListRes findStudents(Member member, String counselStep) {

        List<Student> students = memberRepository.findClassStudents(
                member.getClasses().getId().getSchoolId(),
                member.getClasses().getId().getLevel(),
                member.getClasses().getId().getClassNumber());

        StudentListRes result = new StudentListRes(students, counselStep);

        setCounselStep(result);

        if (counselStep != null) {
            CounselStep step = getByName(counselStep);

            result.setStudents(
                    result.getStudents().stream()
                            .filter(student -> student.getCounselStep().equals(step.getDescription()))
                            .toList()
            );
        }

        return result;
    }

    private void setCounselStep(StudentListRes result) {

        List<Long> studentIds = result.getStudents().stream()
                .map(StudentDto::getStudentId)
                .toList();

        Map<Long, CounselStep> map = memberRepository.findCounselStepByStudentIds(studentIds)
                .stream()
                .collect(Collectors.toMap(
                        CounselStepDto::getStudentId,
                        CounselStepDto::getStep
                ));

        for (StudentDto student : result.getStudents()) {
            CounselStep step = map.get(student.getStudentId());

            if (step != null) {
                student.setCounselStep(step.getDescription());
            }
        }
    }
}
