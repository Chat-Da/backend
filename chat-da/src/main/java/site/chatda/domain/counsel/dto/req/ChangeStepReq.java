package site.chatda.domain.counsel.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeStepReq {

    @NotNull
    private String step;
}
