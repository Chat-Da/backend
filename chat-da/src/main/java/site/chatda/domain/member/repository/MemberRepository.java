package site.chatda.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.chatda.domain.member.dto.CounselStepDto;
import site.chatda.domain.member.entity.Member;
import site.chatda.domain.member.entity.Student;

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
}
