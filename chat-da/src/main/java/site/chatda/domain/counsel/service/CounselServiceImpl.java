package site.chatda.domain.counsel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chatda.domain.counsel.dto.res.CounselListRes;
import site.chatda.domain.counsel.entity.Counsel;
import site.chatda.domain.counsel.enums.CounselStep;
import site.chatda.domain.counsel.repository.CounselRepository;
import site.chatda.domain.member.entity.Member;
import site.chatda.domain.member.entity.Student;
import site.chatda.domain.member.entity.Teacher;
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

        Student student = memberRepository.findStudentByStudentId(studentId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Teacher teacher = memberRepository.findHomeRoomTeacher(student.getMember().getClasses())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        if (!member.getId().equals(teacher.getId())) {
            throw new CustomException(FORBIDDEN);
        }

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
    public CounselListRes findCounsels(Long studentId) {

        List<Counsel> counsels = counselRepository.findAllCounselByStudent(studentId);

        return new CounselListRes(counsels);
    }
}
