package site.chatda.domain.job.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "job_id", columnDefinition = "SMALLINT")
    private Integer id;

    @NotNull
    @Column(length = 30)
    private String name;

    @NotBlank
    @Column(length = 300)
    private String description;
}
