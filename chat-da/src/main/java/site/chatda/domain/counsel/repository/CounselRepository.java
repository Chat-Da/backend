package site.chatda.domain.counsel.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.chatda.domain.counsel.entity.Counsel;

import java.util.List;
import java.util.Optional;

@Repository
public interface CounselRepository extends JpaRepository<Counsel, Long> {

    @EntityGraph(attributePaths = {"student", "teacher"})
    @Query("select c " +
            "from Counsel c " +
            "where c.id = :counselId")
    Optional<Counsel> findCounselWithStudentAndTeacher(@Param("counselId") Long counselId);

    @Query("select c " +
            "from Counsel c " +
            "where c.student.id = :studentId " +
            "order by c.modifiedAt desc ")
    List<Counsel> findRecentCounsel(@Param("studentId") Long studentId, Pageable pageable);

    @Query("select c " +
            "from Counsel c " +
            "join fetch c.teacher t " +
            "join fetch t.member m " +
            "where c.student.id = :studentId " +
            "order by c.modifiedAt desc ")
    List<Counsel> findAllCounselByStudent(@Param("studentId") Long studentId);
}
