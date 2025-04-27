package site.chatda.domain.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.chatda.domain.school.entity.School;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {
}
