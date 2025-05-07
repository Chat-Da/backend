package site.chatda.domain.counsel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.chatda.domain.counsel.entity.TeacherComment;

@Repository
public interface TeacherCommentRepository extends JpaRepository<TeacherComment, Long> {
}
