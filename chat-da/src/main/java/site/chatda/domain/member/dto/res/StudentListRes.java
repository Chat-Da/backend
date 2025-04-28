package site.chatda.domain.member.dto.res;

import lombok.Data;
import site.chatda.domain.member.dto.StudentDto;
import site.chatda.domain.member.entity.Student;

import java.util.List;

@Data
public class StudentListRes {

    private List<StudentDto> students;

    public StudentListRes(List<Student> students, String classInfo) {

        this.students = students.stream()
                .map(StudentDto::new)
                .toList();
    }
}
