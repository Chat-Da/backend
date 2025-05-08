package site.chatda.domain.counsel.dto.res;

import lombok.Data;
import site.chatda.domain.counsel.dto.JobRecommendationDto;
import site.chatda.domain.counsel.dto.StudentInfoDto;
import site.chatda.domain.counsel.dto.TeacherFeedbackDto;
import site.chatda.domain.counsel.dto.TeacherInfoDto;
import site.chatda.domain.counsel.entity.Report;

import java.util.List;

import static site.chatda.global.util.DateFormatter.convertToKoreanFormat;

@Data
public class ReportDetailsRes {

    private StudentInfoDto studentInfo;

    private TeacherInfoDto teacherInfo;

    private Long counselId;

    private String counselDate;

    private String counselStep;

    private String personality;

    private String selfAwareness;

    private String selfAwarenessDescription;

    private List<String> strengths;

    private List<String> weaknesses;

    private List<String> interests;

    private String strengthSummary;

    private String weaknessSummary;

    private List<JobRecommendationDto> jobRecommendations;

    private List<String> growthSuggestions;

    private TeacherFeedbackDto teacherFeedback;

    public ReportDetailsRes(Report report, StudentInfoDto studentInfo, TeacherInfoDto teacherInfo) {
        this.studentInfo = studentInfo;
        this.teacherInfo = teacherInfo;
        this.counselId = report.getId();
        this.counselDate = convertToKoreanFormat(report.getCounsel().getCreatedAt());
        this.counselStep = report.getCounsel().getStep().getDescription();
        this.personality = report.getPersonality();
        this.selfAwareness = report.getSelfAwareness().getLevel();
        this.selfAwarenessDescription = report.getSelfAwarenessDescription();
        this.strengthSummary = report.getStrengthSummary();
        this.weaknessSummary = report.getStrengthSummary();
    }
}
