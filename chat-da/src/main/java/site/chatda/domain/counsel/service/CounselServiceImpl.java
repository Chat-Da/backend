package site.chatda.domain.counsel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chatda.domain.counsel.dto.req.ChangeStepReq;
import site.chatda.domain.counsel.dto.req.CreateReportReq;
import site.chatda.domain.counsel.dto.res.CounselListRes;
import site.chatda.domain.counsel.entity.*;
import site.chatda.domain.counsel.entity.id.InterestId;
import site.chatda.domain.counsel.entity.id.JobRecommendationId;
import site.chatda.domain.counsel.entity.id.StrengthId;
import site.chatda.domain.counsel.entity.id.WeaknessId;
import site.chatda.domain.counsel.enums.CounselStep;
import site.chatda.domain.counsel.repository.BatchRepository;
import site.chatda.domain.counsel.repository.CounselRepository;
import site.chatda.domain.counsel.repository.ReportRepository;
import site.chatda.domain.job.entity.Job;
import site.chatda.domain.job.repository.JobRepository;
import site.chatda.domain.member.entity.Member;
import site.chatda.domain.member.entity.Student;
import site.chatda.domain.member.entity.Teacher;
import site.chatda.domain.member.enums.Role;
import site.chatda.domain.member.repository.MemberRepository;
import site.chatda.global.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import static site.chatda.domain.counsel.enums.CounselStep.*;
import static site.chatda.global.statuscode.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CounselServiceImpl implements CounselService {

    private final CounselRepository counselRepository;
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final JobRepository jobRepository;
    private final BatchRepository batchRepository;

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

        CounselStep counselStep = getByDescription(changeStepReq.getStep());

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

    private void validateCounselStep(Role role, Counsel counsel, CounselStep counselStep) {

        boolean isValid = switch (role) {
            case ROLE_STUDENT -> counsel.getStep() == PENDING && counselStep == IN_PROGRESS;
            case ROLE_TEACHER -> counsel.getStep() == RESULT_WAITING && counselStep == COMPLETED;
            default -> throw new CustomException(BAD_REQUEST);
        };

        if (!isValid) {
            throw new CustomException(BAD_REQUEST);
        }
    }

    @Override
    public void createReport(Long counselId, CreateReportReq createReportReq) {

        Counsel counsel = counselRepository.findCounselWithStudentAndTeacher(counselId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        if (reportRepository.existsById(counselId)) {
            throw new CustomException(REPORT_ALREADY_EXISTS);
        }

        Report report = createNewReport(counsel, createReportReq);

        reportRepository.save(report);
    }

    private Report createNewReport(Counsel counsel, CreateReportReq createReportReq) {

        Report report = createReportReq.toReport(counsel);

        createJobRecommendations(report, createReportReq);
        createStrengths(report, createReportReq.getStrengths());
        createWeaknesses(report, createReportReq.getWeaknesses());
        createInterest(report, createReportReq.getInterests());

        return report;
    }

    private void createJobRecommendations(Report report, CreateReportReq createReportReq) {

        List<Integer> jobSuggestions = createReportReq.getJobSuggestions();
        List<String> jobSuggestionsReasons = createReportReq.getJobSuggestionsReasons();
        List<String> jobGrowthSuggestions = createReportReq.getJobGrowthSuggestions();
        List<Job> jobs = jobRepository.findAllById(jobSuggestions);

        validateJobSuggestions(jobSuggestions, jobSuggestionsReasons, jobGrowthSuggestions, jobs);

        List<JobRecommendation> jobRecommendations = new ArrayList<>();

        for (int i = 0; i < jobSuggestions.size(); i++) {
            Job job = getJob(jobs, jobSuggestions.get(i));

            jobRecommendations.add(
                    JobRecommendation.builder()
                            .id(new JobRecommendationId(jobSuggestions.get(i), report.getId()))
                            .job(job)
                            .report(report)
                            .reason(jobSuggestionsReasons.get(i))
                            .growthSuggestions(jobGrowthSuggestions.get(i))
                            .build());
        }

        batchRepository.saveJobRecommendations(jobRecommendations);
    }

    private static void validateJobSuggestions(List<Integer> jobSuggestions,
                                               List<String> jobSuggestionsReasons,
                                               List<String> jobGrowthSuggestions,
                                               List<Job> jobs) {

        if (jobSuggestions.size() != jobSuggestionsReasons.size() ||
                jobSuggestions.size() != jobGrowthSuggestions.size()) {
            throw new CustomException(BAD_REQUEST);
        }

        if (jobs.size() != jobSuggestions.size()) {
            throw new CustomException(BAD_REQUEST);
        }
    }

    private static Job getJob(List<Job> jobs, Integer jobId) {

        for (Job job : jobs) {
            if (job.getId().equals(jobId)) {
                return job;
            }
        }

        throw new CustomException(BAD_REQUEST);
    }

    private void createStrengths(Report report, List<String> strengthDescriptions) {

        List<Strength> strengths = new ArrayList<>();

        for (int i = 0; i < strengthDescriptions.size(); i++) {
            strengths.add(
                    Strength.builder()
                            .id(new StrengthId(report.getId(), i + 1))
                            .description(strengthDescriptions.get(i))
                            .build()
            );
        }

        batchRepository.saveStrength(strengths);
    }

    private void createWeaknesses(Report report, List<String> weaknessDescriptions) {

        List<Weakness> weaknesses = new ArrayList<>();

        for (int i = 0; i < weaknessDescriptions.size(); i++) {
            weaknesses.add(
                    Weakness.builder()
                            .id(new WeaknessId(report.getId(), i + 1))
                            .description(weaknessDescriptions.get(i))
                            .build()
            );
        }

        batchRepository.saveWeakness(weaknesses);
    }

    private void createInterest(Report report, List<String> interestDescriptions) {

        List<Interest> interests = new ArrayList<>();

        for (int i = 0; i < interestDescriptions.size(); i++) {
            interests.add(
                    Interest.builder()
                            .id(new InterestId(report.getId(), i + 1))
                            .description(interestDescriptions.get(i))
                            .build()
            );
        }

        batchRepository.saveInterest(interests);
    }
}
