package site.chatda.domain.counsel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.util.EnumUtils;
import site.chatda.domain.counsel.dto.req.ChangeStepReq;
import site.chatda.domain.counsel.dto.res.CounselListRes;
import site.chatda.domain.counsel.entity.Counsel;
import site.chatda.domain.counsel.enums.CounselStep;
import site.chatda.domain.counsel.repository.CounselRepository;
import site.chatda.domain.member.entity.Member;
import site.chatda.domain.member.entity.Student;
import site.chatda.domain.member.entity.Teacher;
import site.chatda.domain.member.enums.Role;
import site.chatda.domain.member.repository.MemberRepository;
import site.chatda.global.exception.CustomException;

import java.util.List;

import static site.chatda.domain.counsel.enums.CounselStep.*;
import static site.chatda.global.statuscode.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CounselServiceImpl implements CounselService {

    private final CounselRepository counselRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void applyCounsel(Member member) {

        Student student = memberRepository.findStudentByStudentId(member.getId())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Teacher teacher = memberRepository.findHomeRoomTeacher(member.getClasses())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Counsel recentCounsel = getRecentCounselFromDB(member.getId());

        if (!hasNoOpenCounsel(recentCounsel)) {
            throw new CustomException(COUNSEL_ALREADY_EXISTS);
        }

        Counsel counsel = createNewCounsel(member, student, teacher, REQUESTED);

        counselRepository.save(counsel);
    }

    @Override
    @Transactional
    public void openCounsel(Member member, Long studentId) {

        checkMyStudent(member.getId(), studentId);

        Student student = memberRepository.findStudentByStudentId(studentId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Teacher teacher = memberRepository.findTeacherByMemberId(member.getId())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Counsel counsel = getRecentCounselFromDB(studentId);

        Counsel recentCounsel = getRecentCounsel(member, counsel, student, teacher);

        counselRepository.save(recentCounsel);
    }

    private Counsel getRecentCounselFromDB(Long studentId) {

        List<Counsel> counsel = counselRepository.findRecentCounsel(studentId, Pageable.ofSize(1));

        if (counsel.isEmpty()) {
            return null;
        } else {
            return counsel.getFirst();
        }
    }

    private Counsel getRecentCounsel(Member member, Counsel counsel, Student student, Teacher teacher) {

        if (hasNoOpenCounsel(counsel)) {
            counsel = createNewCounsel(member, student, teacher, PENDING);
        } else if (counsel.getStep() == REQUESTED) {
            counsel.changeStep(PENDING);
        } else {
            throw new CustomException(COUNSEL_ALREADY_EXISTS);
        }

        return counsel;
    }

    private boolean hasNoOpenCounsel(Counsel counsel) {

        return counsel == null || counsel.getStep() == COMPLETED;
    }

    private Counsel createNewCounsel(Member member, Student student, Teacher teacher, CounselStep step) {

        return Counsel.builder()
                .grade(member.getClasses().getGrade())
                .student(student)
                .teacher(teacher)
                .step(step)
                .build();
    }

    @Override
    public CounselListRes findStudentCounsels(Member member, Long studentId) {

        checkMyStudent(member.getId(), studentId);

        return findCounsels(studentId);
    }

    private void checkMyStudent(Long teacherId, Long studentId) {

        Student student = memberRepository.findStudentByStudentId(studentId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Teacher teacher = memberRepository.findHomeRoomTeacher(student.getMember().getClasses())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        if (!teacher.getId().equals(teacherId)) {
            throw new CustomException(FORBIDDEN);
        }
    }

    @Override
    public CounselListRes findCounsels(Long studentId) {

        List<Counsel> counsels = counselRepository.findAllCounselByStudent(studentId);

        return new CounselListRes(counsels);
    }

    @Override
    @Transactional
    public void changeCounselStep(Member member, Long counselId, ChangeStepReq changeStepReq) {

        Counsel counsel = counselRepository.findCounselWithStudentAndTeacher(counselId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        checkMyCounsel(member, counsel);

        CounselStep counselStep = validateCounselStep(changeStepReq.getStep());

        validateCounselStep(member.getRole(), counsel, counselStep);

        counsel.changeStep(counselStep);
    }

    private void checkMyCounsel(Member member, Counsel counsel) {

        switch (member.getRole()) {
            case ROLE_STUDENT -> {
                if (!counsel.getStudent().getId().equals(member.getId())) {
                    throw new CustomException(FORBIDDEN);
                }
            }

            case ROLE_TEACHER -> {
                if (!counsel.getTeacher().getId().equals(member.getId())) {
                    throw new CustomException(FORBIDDEN);
                }
            }
        }
    }

    private CounselStep validateCounselStep(String counselStep) {

        try {
            return EnumUtils.findEnumInsensitiveCase(CounselStep.class, counselStep.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(BAD_REQUEST);
        }
    }

    private void validateCounselStep(Role role, Counsel counsel, CounselStep counselStep) {

        boolean isValid = switch (role) {
            case ROLE_STUDENT -> counsel.getStep() == PENDING && counselStep == IN_PROGRESS;
            case ROLE_TEACHER -> counsel.getStep() == RESULT_WAITING && counselStep == COMPLETED;
        };

        if (!isValid) {
            throw new CustomException(BAD_REQUEST);
        }
    }
}
