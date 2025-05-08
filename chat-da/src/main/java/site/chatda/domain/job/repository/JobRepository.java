package site.chatda.domain.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.chatda.domain.counsel.entity.JobRecommendation;
import site.chatda.domain.job.entity.Job;
import site.chatda.domain.job.entity.JobSkill;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

    @Query("select jr " +
            "from JobRecommendation jr " +
            "where jr.id.counselId = :counselId")
    List<JobRecommendation> findJobRecommendationsByCounselId(@Param("counselId") Long counselId);

    @Query("select js " +
            "from JobSkill js " +
            "join fetch js.job j " +
            "join fetch js.skill s " +
            "where j.id in :jobIds")
    List<JobSkill> findJobSkills(@Param("jobIds") List<Integer> jobIds);
}
