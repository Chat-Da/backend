package site.chatda.domain.counsel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.chatda.domain.counsel.dto.TeacherGuidanceDto;
import site.chatda.domain.counsel.entity.*;

import java.util.List;
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

    @Query("select s " +
            "from Strength s " +
            "where s.id.counselId = :counselId " +
            "order by s.id.seq")
    List<Strength> findStrengths(@Param("counselId") Long counselId);

    @Query("select w " +
            "from Weakness w " +
            "where w.id.counselId = :counselId " +
            "order by w.id.seq")
    List<Weakness> findWeaknesses(@Param("counselId") Long counselId);

    @Query("select i " +
            "from Interest i " +
            "where i.id.counselId = :counselId " +
            "order by i.id.seq")
    List<Interest> findInterests(@Param("counselId") Long counselId);

    @Query("select gs " +
            "from GrowthSuggestion gs " +
            "where gs.id.counselId = :counselId " +
            "order by gs.id.seq")
    List<GrowthSuggestion> findGrowthSuggestions(@Param("counselId") Long counselId);

    @Query("select tc.content " +
            "from TeacherComment tc " +
            "where tc.report.id = :reportId")
    Optional<String> findTeacherComment(@Param("reportId") Long reportId);

    @Query("select js.content " +
            "from TeacherJobSuggestion js " +
            "where js.report.id = :reportId")
    Optional<String> findTeacherJobSuggestion(@Param("reportId") Long reportId);

    @Query("select new site.chatda.domain.counsel.dto.TeacherGuidanceDto(" +
            "   tg.id.seq, tg.content) " +
            "from TeacherGuidance tg " +
            "where tg.report.id = :reportId")
    List<TeacherGuidanceDto> findTeacherGuidance(@Param("reportId") Long reportId);

}
