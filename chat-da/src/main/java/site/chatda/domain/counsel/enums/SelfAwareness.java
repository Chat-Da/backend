package site.chatda.domain.counsel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SelfAwareness {
    HIGH("high level"),
    MIDDLE("middle level"),
    LOW("low level")
    ;

    private final String description;
}
