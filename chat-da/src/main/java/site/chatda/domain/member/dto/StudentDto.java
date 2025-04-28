package site.chatda.domain.member.dto;

import lombok.Data;
import site.chatda.domain.member.entity.Student;

@Data
public class StudentDto {

    private Long studentId;

    private String name;

    private String src;

    private String schoolName;

    private Integer level;

    private Integer classNumber;

    private Integer studentNumber;

    private String counselStep;

    public StudentDto(Student student) {
        this.studentId = student.getId();
        this.name = student.getMember().getName();
        this.src = student.getSrc();
        this.schoolName = student.getMember().getClasses().getGrade().getSchool().getName();
        this.level = student.getMember().getClasses().getId().getLevel();
        this.classNumber = student.getMember().getClasses().getId().getClassNumber();
        this.studentNumber = student.getStudentNumber();
        this.counselStep = "상담 없음";
    }
}
