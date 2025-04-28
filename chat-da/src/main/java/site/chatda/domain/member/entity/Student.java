package site.chatda.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Column(columnDefinition = "TINYINT")
    private Integer studentNumber;

    @NotBlank
    @Column(length = 100)
    private String src;
}
