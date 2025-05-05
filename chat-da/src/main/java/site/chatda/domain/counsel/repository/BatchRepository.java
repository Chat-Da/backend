package site.chatda.domain.counsel.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import site.chatda.domain.counsel.entity.*;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BatchRepository {
    private final JdbcTemplate jdbcTemplate;

    public void saveStrength(List<Strength> strengths){
        String sql = "insert into strength(seq, counsel_id, description) values (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                strengths,
                strengths.size(),
                (PreparedStatement ps, Strength strength) -> {
                    ps.setString(1, String.valueOf(strength.getId().getSeq()));
                    ps.setString(2, String.valueOf(strength.getId().getCounselId()));
                    ps.setString(3, strength.getDescription());
                });
    }

    public void saveWeakness(List<Weakness> weaknesses){
        String sql = "insert into weakness(seq, counsel_id, description) values (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                weaknesses,
                weaknesses.size(),
                (PreparedStatement ps, Weakness weakness) -> {
                    ps.setString(1, String.valueOf(weakness.getId().getSeq()));
                    ps.setString(2, String.valueOf(weakness.getId().getCounselId()));
                    ps.setString(3, weakness.getDescription());
                });
    }

    public void saveInterest(List<Interest> interests){
        String sql = "insert into interest(seq, counsel_id, description) values (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                interests,
                interests.size(),
                (PreparedStatement ps, Interest interest) -> {
                    ps.setString(1, String.valueOf(interest.getId().getSeq()));
                    ps.setString(2, String.valueOf(interest.getId().getCounselId()));
                    ps.setString(3, interest.getDescription());
                });
    }

    public void saveGrowthSuggestions(List<GrowthSuggestion> growthSuggestions){
        String sql = "insert into growth_suggestion(seq, counsel_id, description) values (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                growthSuggestions,
                growthSuggestions.size(),
                (PreparedStatement ps, GrowthSuggestion growthSuggestion) -> {
                    ps.setString(1, String.valueOf(growthSuggestion.getId().getSeq()));
                    ps.setString(2, String.valueOf(growthSuggestion.getId().getCounselId()));
                    ps.setString(3, growthSuggestion.getDescription());
                });
    }

    public void saveJobRecommendations(List<JobRecommendation> jobRecommendations){
        String sql = "insert into job_recommendation(job_id, counsel_id, reason, growth_suggestions) values (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                jobRecommendations,
                jobRecommendations.size(),
                (PreparedStatement ps, JobRecommendation jobRecommendation) -> {
                    ps.setString(1, String.valueOf(jobRecommendation.getId().getJobId()));
                    ps.setString(2, String.valueOf(jobRecommendation.getId().getCounselId()));
                    ps.setString(3, jobRecommendation.getReason());
                    ps.setString(4, jobRecommendation.getGrowthSuggestions());
                });
    }
}
