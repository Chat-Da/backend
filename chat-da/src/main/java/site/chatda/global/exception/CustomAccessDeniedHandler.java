package site.chatda.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import site.chatda.global.dto.ResponseData;
import site.chatda.global.dto.ResponseHeader;

import java.io.IOException;

import static site.chatda.global.statuscode.ErrorCode.FORBIDDEN;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        ResponseData res = new ResponseData(new ResponseHeader(FORBIDDEN.getMessage()), null);
        response.getWriter().write(mapper.writeValueAsString(res));
    }
}
