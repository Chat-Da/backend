package site.chatda.global.argument_resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import site.chatda.domain.member.entity.Student;
import site.chatda.global.exception.CustomException;
import site.chatda.global.jwt.userdetails.StudentDetails;

import static site.chatda.global.statuscode.ErrorCode.FORBIDDEN;

public class StudentLoginArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginStudentAnnotation = parameter.hasParameterAnnotation(LoginStudent.class);
        boolean hasStudentType = Student.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginStudentAnnotation && hasStudentType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.equals("anonymousUser")) {
            return null;
        }

        try {
            StudentDetails userDetails = (StudentDetails) principal;

            return userDetails.getStudent();

        } catch (ClassCastException e) {
            throw new CustomException(FORBIDDEN);
        }
    }
}
