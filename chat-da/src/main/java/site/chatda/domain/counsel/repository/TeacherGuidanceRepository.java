package site.chatda.domain.counsel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.chatda.domain.counsel.entity.TeacherGuidance;
import site.chatda.domain.counsel.entity.id.TeacherGuidanceId;

@Repository
public interface TeacherGuidanceRepository extends JpaRepository<TeacherGuidance, TeacherGuidanceId> {
}
