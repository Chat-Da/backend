package site.chatda.domain.counsel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.chatda.domain.counsel.entity.TeacherJobSuggestion;

@Repository
public interface TeacherJobSuggestionRepository extends JpaRepository<TeacherJobSuggestion, Long> {
}
