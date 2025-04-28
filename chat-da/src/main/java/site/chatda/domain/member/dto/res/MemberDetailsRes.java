package site.chatda.domain.member.dto.res;

import lombok.Data;
import site.chatda.domain.member.entity.Member;
import site.chatda.domain.school.entity.School;

@Data
public class MemberDetailsRes {

    private String schoolName;

    private Integer level;

    private Integer classNumber;

    private String name;

    public MemberDetailsRes(School school, Member member) {
        this.schoolName = school.getName();
        this.level = member.getClasses().getId().getLevel();
        this.classNumber = member.getClasses().getId().getClassNumber();
        this.name = member.getName();
    }
}
