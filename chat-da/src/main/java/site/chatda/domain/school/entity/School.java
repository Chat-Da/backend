package site.chatda.domain.school.entity;

import jakarta.persistence.*;
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
public class School {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "school_id", columnDefinition = "SMALLINT")
    private Integer id;

    @NotNull
    @Column(length = 30)
    private String name;
}
