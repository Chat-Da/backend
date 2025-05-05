package site.chatda.domain.counsel.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import site.chatda.domain.counsel.entity.Counsel;
import site.chatda.domain.counsel.entity.Report;
import site.chatda.domain.counsel.enums.SelfAwareness;

import java.util.List;

@Data
public class CreateReportReq {

    @NotBlank
    private String personality;

    @NotBlank
    private String selfAwareness;

    @NotBlank
    private String selfAwarenessDescription;

    @NotBlank
    private String strengthSummary;

    @NotBlank
    private String weaknessSummary;

    @Size(min = 1, max = 3)
    private List<String> strengths;

    @Size(min = 1, max = 3)
    private List<String> weaknesses;

    @Size(min = 1, max = 3)
    private List<String> interests;

    @Size(min = 1, max = 3)
    private List<String> growthSuggestions;

    @Size(min = 1, max = 3)
    private List<Integer> jobSuggestions;

    @Size(min = 1, max = 3)
    private List<String> jobSuggestionReasons;

    @Size(min = 1, max = 3)
    private List<String> jobGrowthSuggestions;

    public Report toReport(Counsel counsel) {

        return Report.builder()
                .counsel(counsel)
                .personality(personality)
                .selfAwareness(SelfAwareness.getByName(selfAwareness))
                .selfAwarenessDescription(selfAwarenessDescription)
                .strengthSummary(strengthSummary)
                .weaknessSummary(weaknessSummary)
                .build();
    }
}
