package site.chatda.domain.counsel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.chatda.global.exception.CustomException;

import static site.chatda.global.statuscode.ErrorCode.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum SelfAwareness {
    HIGH("high level"),
    MIDDLE("middle level"),
    LOW("low level")
    ;

    private final String level;

    public static SelfAwareness getByLevel(String description) {

        for (SelfAwareness level : SelfAwareness.values()) {
            if (description.equals(level.getLevel())) {
                return level;
            }
        }

        throw new CustomException(BAD_REQUEST);
    }
}
