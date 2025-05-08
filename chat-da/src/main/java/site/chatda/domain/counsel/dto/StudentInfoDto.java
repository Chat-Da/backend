package site.chatda.domain.counsel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentInfoDto {

    private String name;

    private String schoolName;

    private Integer grade;
}
