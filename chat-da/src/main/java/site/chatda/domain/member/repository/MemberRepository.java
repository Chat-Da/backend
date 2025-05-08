package site.chatda.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.chatda.domain.counsel.dto.StudentInfoDto;
import site.chatda.domain.counsel.dto.TeacherInfoDto;
import site.chatda.domain.member.dto.CounselStepDto;
import site.chatda.domain.member.entity.Member;
import site.chatda.domain.member.entity.Student;
import site.chatda.domain.member.entity.Teacher;
import site.chatda.domain.school.entity.Classes;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m " +
            "from Member m " +
            "join fetch m.classes c " +
            "join fetch c.grade g " +
            "join fetch g.school " +
            "where m.uuid = :uuid")
    Optional<Member> findMemberByUuid(@Param("uuid") String uuid);

    @Query("select s " +
            "from Student s " +
            "join fetch s.member m " +
            "join fetch m.classes c " +
            "where c.school.id = :schoolId " +
            "and m.classes.id.level = :level " +
            "and c.id.classNumber = :classNumber " +
            "order by s.studentNumber")
    List<Student> findClassStudents(@Param("schoolId") Integer schoolId,
                                    @Param("level") Integer level,
                                    @Param("classNumber") Integer classNumber);

    @Query("select new site.chatda.domain.member.dto.CounselStepDto(c.student.id, c.step) " +
            "from Counsel c " +
            "where c.student.id in :studentIds " +
            "and c.modifiedAt = (" +
            "   select max(c2.modifiedAt) " +
            "   from Counsel c2 " +
            "   where c2.student.id = c.student.id) " +
            "order by c.student.studentNumber")
    List<CounselStepDto> findCounselStepByStudentIds(@Param("studentIds") List<Long> studentIds);

    @Query("select t " +
            "from Teacher t " +
            "where t.member.id = :memberId")
    Optional<Teacher> findTeacherByMemberId(@Param("memberId") Long memberId);

    @Query("select s " +
            "from Student s " +
            "join fetch s.member m " +
            "join fetch m.classes c " +
            "join fetch c.grade g " +
            "join fetch g.school " +
            "where s.id = :studentId")
    Optional<Student> findStudentByStudentId(@Param("studentId") Long studentId);

    @Query("select t " +
            "from Teacher t " +
            "join fetch t.member m " +
            "join fetch m.classes c " +
            "where c = :classes ")
    Optional<Teacher> findHomeRoomTeacher(@Param("classes") Classes classes);

    @Query("select new site.chatda.domain.counsel.dto.StudentInfoDto(" +
            "   m.name, sc.name, c.id.level) " +
            "from Student s " +
            "join s.member m " +
            "join m.classes c " +
            "join c.school sc " +
            "where s.id = :studentId")
    Optional<StudentInfoDto> findStudentInfo(@Param("studentId") Long studentId);

    @Query("select new site.chatda.domain.counsel.dto.TeacherInfoDto(" +
            "   m.name, s.name) " +
            "from Teacher t " +
            "join t.member m " +
            "join m.classes c " +
            "join c.school s " +
            "where t.id = :teacherId")
    Optional<TeacherInfoDto> findTeacherInfo(@Param("teacherId") Long teacherId);
}
