package site.chatda.domain.counsel.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.chatda.domain.counsel.entity.Counsel;

import java.util.List;

@Repository
public interface CounselRepository extends JpaRepository<Counsel, Long> {

    @Query("select c " +
            "from Counsel c " +
            "where c.student.id = :studentId " +
            "order by c.modifiedAt desc ")
    List<Counsel> findRecentCounsel(@Param("studentId") Long studentId, Pageable pageable);
}
