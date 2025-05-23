package site.chatda.global.statuscode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(400,"Bad Request"),
    INVALID_ARGUMENT(400, "Invalid Argument"),
    COUNSEL_ALREADY_EXISTS(400, "이미 열려있는 상담이 있습니다."),
    REPORT_ALREADY_EXISTS(400, "이미 생성된 보고서가 있습니다"),
    UNAUTHORIZED(401, "Unauthorized"),
    INVALID_TOKEN(401, "Invalid Token"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    ;

    private final int httpStatusCode;

    private final String message;

}

