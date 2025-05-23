package site.chatda.domain.counsel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.chatda.domain.counsel.dto.*;
import site.chatda.domain.counsel.dto.req.*;
import site.chatda.domain.counsel.dto.res.CounselListRes;
import site.chatda.domain.counsel.dto.res.ReportDetailsRes;
import site.chatda.domain.counsel.entity.*;
import site.chatda.domain.counsel.entity.id.*;
import site.chatda.domain.counsel.enums.CounselStep;
import site.chatda.domain.counsel.repository.*;
import site.chatda.domain.job.entity.Job;
import site.chatda.domain.job.entity.JobSkill;
import site.chatda.domain.job.repository.JobRepository;
import site.chatda.domain.member.entity.Member;
import site.chatda.domain.member.entity.Student;
import site.chatda.domain.member.entity.Teacher;
import site.chatda.domain.member.enums.Role;
import site.chatda.domain.member.repository.MemberRepository;
import site.chatda.global.exception.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static site.chatda.domain.counsel.enums.CounselStep.*;
import static site.chatda.domain.member.enums.Role.ROLE_TEACHER;
import static site.chatda.global.statuscode.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CounselServiceImpl implements CounselService {

    private final CounselRepository counselRepository;
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final JobRepository jobRepository;
    private final TeacherCommentRepository teacherCommentRepository;
    private final TeacherJobSuggestionRepository teacherJobSuggestionRepository;
    private final TeacherGuidanceRepository teacherGuidanceRepository;
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

        CounselStep counselStep = getByName(changeStepReq.getStep());

        validateCounselStep(member.getRole(), counsel, counselStep);

        counsel.changeStep(counselStep);
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
    @Transactional
    public void createReport(Long counselId, CreateReportReq createReportReq) {

        Counsel counsel = counselRepository.findCounselWithStudentAndTeacher(counselId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        if (counsel.getStep() != IN_PROGRESS) {
            throw new CustomException(BAD_REQUEST);
        }

        if (reportRepository.existsById(counselId)) {
            throw new CustomException(REPORT_ALREADY_EXISTS);
        }

        createNewReport(counsel, createReportReq);

        counsel.changeStep(RESULT_WAITING);
    }

    private void createNewReport(Counsel counsel, CreateReportReq createReportReq) {

        Report report = createReportReq.toReport(counsel);

        reportRepository.save(report);
        reportRepository.flush();

        createJobRecommendations(report, createReportReq);
        createStrengths(report, createReportReq.getStrengths());
        createWeaknesses(report, createReportReq.getWeaknesses());
        createInterest(report, createReportReq.getInterests());
        createGrowthSuggestions(report, createReportReq.getGrowthSuggestions());
    }

    private void createJobRecommendations(Report report, CreateReportReq createReportReq) {

        List<Integer> jobSuggestions = createReportReq.getJobSuggestions();
        List<String> jobSuggestionsReasons = createReportReq.getJobSuggestionReasons();
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

    private void createGrowthSuggestions(Report report, List<String> growthSuggestionContents) {

        List<GrowthSuggestion> growthSuggestions = new ArrayList<>();

        for (int i = 0; i < growthSuggestionContents.size(); i++) {
            growthSuggestions.add(
                    GrowthSuggestion.builder()
                            .id(new GrowthSuggestionId(report.getId(), i + 1))
                            .content(growthSuggestionContents.get(i))
                            .build()
            );
        }

        batchRepository.saveGrowthSuggestions(growthSuggestions);
    }

    @Override
    @Transactional
    public void saveTeacherComment(Member member, Long counselId, SaveTeacherCommentReq saveTeacherCommentReq) {

        Report report = reportRepository.findByCounselId(counselId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Counsel counsel = report.getCounsel();

        checkMyCounsel(member, counsel);

        if (counsel.getStep() != RESULT_WAITING) {
            throw new CustomException(BAD_REQUEST);
        }

        TeacherComment teacherComment = teacherCommentRepository.findById(counselId)
                .map(tc -> {
                    tc.updateContent(saveTeacherCommentReq.getContent());
                    return tc;
                })
                .orElseGet(() -> saveTeacherCommentReq.toTeacherComment(report));

        teacherCommentRepository.save(teacherComment);
    }

    @Override
    @Transactional
    public void saveTeacherJobSuggestion(Member member, Long counselId,
                                         SaveTeacherJobSuggestionReq saveTeacherJobSuggestionReq) {

        Report report = reportRepository.findByCounselId(counselId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Counsel counsel = report.getCounsel();

        checkMyCounsel(member, counsel);

        if (counsel.getStep() != RESULT_WAITING) {
            throw new CustomException(BAD_REQUEST);
        }

        TeacherJobSuggestion teacherJobSuggestion = teacherJobSuggestionRepository.findById(counselId)
                .map(js -> {
                    js.updateContent(saveTeacherJobSuggestionReq.getContent());
                    return js;
                })
                .orElseGet(() -> saveTeacherJobSuggestionReq.toTeacherJobSuggestion(report));

        teacherJobSuggestionRepository.save(teacherJobSuggestion);
    }

    @Override
    @Transactional
    public void saveTeacherGuidance(Member member, Long counselId, Integer seq,
                                    SaveTeacherGuidanceReq saveTeacherGuidanceReq) {

        if (seq < 1 || seq > 3) {
            throw new CustomException(BAD_REQUEST);
        }

        Report report = reportRepository.findByCounselId(counselId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Counsel counsel = report.getCounsel();

        checkMyCounsel(member, counsel);

        if (counsel.getStep() != RESULT_WAITING) {
            throw new CustomException(BAD_REQUEST);
        }

        TeacherGuidance teacherGuidance = teacherGuidanceRepository.findById(new TeacherGuidanceId(report.getId(), seq))
                .map(tg -> {
                    tg.updateContent(saveTeacherGuidanceReq.getContent());
                    return tg;
                })
                .orElseGet(() -> saveTeacherGuidanceReq.teacherGuidance(report, seq));

        teacherGuidanceRepository.save(teacherGuidance);
    }

    @Override
    public ReportDetailsRes getReportDetails(Member member, Long counselId) {

        Report report = reportRepository.findByCounselId(counselId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Counsel counsel = report.getCounsel();

        checkMyCounsel(member, counsel);

        StudentInfoDto studentInfo = memberRepository.findStudentInfo(counsel.getStudent().getId())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        TeacherInfoDto teacherInfo = memberRepository.findTeacherInfo(counsel.getTeacher().getId())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        ReportDetailsRes result = new ReportDetailsRes(report, studentInfo, teacherInfo);

        setJobRecommendations(result);
        setStrengths(result);
        setWeaknesses(result);
        setInterests(result);
        setGrowthSuggestions(result);

        if (counsel.getStep() == COMPLETED || member.getRole() == ROLE_TEACHER) {
            setTeacherFeedback(result);
        }

        return result;
    }

    private void setJobRecommendations(ReportDetailsRes result) {

        List<JobRecommendation> jobRecommendationList =
                jobRepository.findJobRecommendationsByCounselId(result.getCounselId());

        Map<Integer, List<JobSkill>> jobSkills = jobRepository.findJobSkills(
                        jobRecommendationList.stream()
                                .map(jobRecommendation -> jobRecommendation.getJob().getId())
                                .toList())
                .stream()
                .collect(Collectors.groupingBy(jobSkill -> jobSkill.getJob().getId()));

        List<JobRecommendationDto> jobRecommendations = jobRecommendationList.stream()
                .map(jobRecommendation ->
                        new JobRecommendationDto(jobRecommendation, jobSkills.get(jobRecommendation.getJob().getId())))
                .toList();

        result.setJobRecommendations(jobRecommendations);
    }

    private void setStrengths(ReportDetailsRes result) {

        List<String> strengths = reportRepository.findStrengths(result.getCounselId()).stream()
                .map(Strength::getDescription)
                .toList();

        result.setStrengths(strengths);
    }

    private void setWeaknesses(ReportDetailsRes result) {

        List<String> weaknesses = reportRepository.findWeaknesses(result.getCounselId()).stream()
                .map(Weakness::getDescription)
                .toList();

        result.setWeaknesses(weaknesses);
    }

    private void setInterests(ReportDetailsRes result) {

        List<String> interests = reportRepository.findInterests(result.getCounselId()).stream()
                .map(Interest::getDescription)
                .toList();

        result.setInterests(interests);
    }

    private void setGrowthSuggestions(ReportDetailsRes result) {

        List<String> growthSuggestions = reportRepository.findGrowthSuggestions(result.getCounselId()).stream()
                .map(GrowthSuggestion::getContent)
                .toList();

        result.setGrowthSuggestions(growthSuggestions);
    }

    private void setTeacherFeedback(ReportDetailsRes result) {

        Long reportId = result.getCounselId();

        TeacherFeedbackDto teacherFeedback = new TeacherFeedbackDto();

        Optional<String> comment = reportRepository.findTeacherComment(reportId);
        Optional<String> jobSuggestion = reportRepository.findTeacherJobSuggestion(reportId);

        List<TeacherGuidanceDto> teacherGuidance = reportRepository.findTeacherGuidance(reportId).stream()
                .map(guidance -> new TeacherGuidanceDto(guidance.getSeq(), guidance.getContent()))
                .toList();

        comment.ifPresent(teacherFeedback::setComment);
        jobSuggestion.ifPresent(teacherFeedback::setJobSuggestion);
        teacherFeedback.setGuidance(teacherGuidance);

        result.setTeacherFeedback(teacherFeedback);
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
}
