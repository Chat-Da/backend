package site.chatda.domain.counsel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
