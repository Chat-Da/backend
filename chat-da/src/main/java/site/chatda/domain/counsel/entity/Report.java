package site.chatda.domain.counsel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.chatda.domain.counsel.enums.SelfAwareness;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @Column(name = "counsel_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    @OneToOne
    @MapsId("memberId")
    @JoinColumn(name = "counsel_id")
    private Counsel counsel;

    @NotNull
    @Column(length = 500)
    private String personality;

    @NotNull
    @Enumerated(STRING)
    private SelfAwareness selfAwareness;

    @NotNull
    @Column(length = 500)
    private String selfAwarenessDescription;

    @NotNull
    @Column(length = 500)
    private String strengthSummary;

    @NotNull
    @Column(length = 500)
    private String weaknessSummary;
}
