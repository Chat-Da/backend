package site.chatda.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import site.chatda.domain.member.enums.Role;
import site.chatda.domain.school.entity.Classes;
import site.chatda.global.entity.BaseEntity;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = " UPDATE Member SET is_deleted = true WHERE member_id = ? ")
@SQLRestriction("is_deleted = false")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    @NotNull
    @Column(columnDefinition = "CHAR(36)", unique = true)
    private String uuid;

    @NotNull
    @Column(length = 20)
    private String name;

    @NotNull
    @Enumerated(STRING)
    private Role role;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumns(value = {
            @JoinColumn(name = "school_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "level", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "class_number", nullable = false, insertable = false, updatable = false)
    })
    private Classes classes;
}
