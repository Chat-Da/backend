package site.chatda.domain.counsel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRecommendationId implements Serializable {

    @NotNull
    @Column(columnDefinition = "SMALLINT")
    private Integer jobId;

    @NotNull
    @Column(columnDefinition = "INT UNSIGNED")
    private Long counselId;
}
