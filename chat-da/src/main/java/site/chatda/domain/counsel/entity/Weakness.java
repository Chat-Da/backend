package site.chatda.domain.counsel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.chatda.domain.counsel.entity.id.WeaknessId;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Weakness {

    @EmbeddedId
    private WeaknessId id;

    @NotNull
    @Column(length = 30)
    private String description;
}
