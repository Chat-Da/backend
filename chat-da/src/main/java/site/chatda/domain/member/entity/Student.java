package site.chatda.domain.member.entity;

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
public class Student {
    @Id
    @Column(name = "member_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    @OneToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;
}
