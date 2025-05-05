package site.chatda.global.jwt.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.chatda.global.dto.ResponseDto;
import site.chatda.global.jwt.JwtUtils;

import static site.chatda.global.statuscode.SuccessCode.OK;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtils jwtUtils;

    @PostMapping("/login/students")
    public ResponseDto<Void> loginStudent(HttpServletResponse response) {

        String uuid = "78954910-7440-4241-bd9d-ca3abc44d291";

        String token = jwtUtils.createToken(uuid);

        response.setHeader("Authorization", token);

        return ResponseDto.success(OK);
    }

    @PostMapping("/login/teachers")
    public ResponseDto<Void> loginTeacher(HttpServletResponse response) {

        String uuid = "5a1e826e-2a44-4fea-98b2-bb96887b9737";

        String token = jwtUtils.createToken(uuid);

        response.setHeader("Authorization", token);

        return ResponseDto.success(OK);
    }

    @PostMapping("/login/admin")
    public ResponseDto<Void> loginAdmin(HttpServletResponse response) {

        String uuid = "eca58d22-f409-465b-a179-b5d527b4f687";

        String token = jwtUtils.createToken(uuid);

        response.setHeader("Authorization", token);

        return ResponseDto.success(OK);
    }
}
