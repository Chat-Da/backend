package site.chatda.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.chatda.domain.member.entity.Member;
import site.chatda.domain.member.entity.Student;
import site.chatda.domain.member.entity.Teacher;

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
            "where s.id = :memberId")
    Optional<Student> findStudentByMemberId(@Param("memberId") Long memberId);

    @Query("select t " +
            "from Teacher t " +
            "where t.id = :memberId")
    Optional<Teacher> findTeacherByMemberId(@Param("memberId") Long memberId);
}
