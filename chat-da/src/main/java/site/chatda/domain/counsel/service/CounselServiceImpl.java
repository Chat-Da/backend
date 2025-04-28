package site.chatda.domain.counsel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chatda.domain.counsel.entity.Counsel;
import site.chatda.domain.counsel.repository.CounselRepository;
import site.chatda.domain.member.entity.Member;
import site.chatda.domain.member.entity.Student;
import site.chatda.domain.member.entity.Teacher;
import site.chatda.domain.member.repository.MemberRepository;
import site.chatda.global.exception.CustomException;

import java.util.List;

import static site.chatda.domain.counsel.enums.CounselStep.COMPLETED;
import static site.chatda.domain.counsel.enums.CounselStep.REQUESTED;
import static site.chatda.global.statuscode.ErrorCode.COUNSEL_ALREADY_EXISTS;
import static site.chatda.global.statuscode.ErrorCode.NOT_FOUND;

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

        List<Counsel> recentCounsel = counselRepository.findByStudentId(member.getId(), Pageable.ofSize(1));

        if (!recentCounsel.isEmpty() && recentCounsel.getFirst().getStep() != COMPLETED) {
            throw new CustomException(COUNSEL_ALREADY_EXISTS);
        }

        Counsel counsel = Counsel.builder()
                .grade(member.getClasses().getGrade())
                .student(student)
                .teacher(teacher)
                .step(REQUESTED)
                .build();

        counselRepository.save(counsel);
    }
}
