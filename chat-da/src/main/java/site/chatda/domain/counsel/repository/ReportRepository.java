package site.chatda.domain.counsel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.chatda.domain.counsel.entity.Report;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("select r " +
            "from Report r " +
            "join fetch r.counsel c " +
            "join fetch c.student " +
            "join fetch c.teacher " +
            "where r.id = :counselId")
    Optional<Report> findByCounselId(@Param("counselId") Long counselId);
}
