package site.chatda.global.statuscode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(400,"Bad Request"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    INVALID_TOKEN(403, "Invalid Token"),
    INVALID_ARGUMENT(400, "Invalid Argument"),
    ;

    private final int httpStatusCode;

    private final String message;

}

