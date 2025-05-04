package site.chatda.domain.counsel.entity.id;


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
public class WeaknessId implements Serializable {

    private Long counselId;

    @NotNull
    @Column(columnDefinition = "TINYINT")
    private Integer seq;
}
