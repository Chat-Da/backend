package site.chatda.domain.school.entity;

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
public class ClassesId implements Serializable {

    private Integer schoolId;

    @NotNull
    @Column(columnDefinition = "TINYINT")
    private Integer level;

    @NotNull
    @Column(columnDefinition = "TINYINT")
    private Integer classNumber;
}
