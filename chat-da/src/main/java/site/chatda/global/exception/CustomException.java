package site.chatda.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.chatda.global.statuscode.ErrorCode;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    ErrorCode errorCode;
}
