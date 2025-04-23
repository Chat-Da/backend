package site.chatda.global.argument_resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import site.chatda.domain.member.entity.Teacher;
import site.chatda.global.exception.CustomException;
import site.chatda.global.jwt.userdetails.TeacherDetails;

import static site.chatda.global.statuscode.ErrorCode.FORBIDDEN;

public class TeacherLoginArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginTeacherAnnotation = parameter.hasParameterAnnotation(LoginTeacher.class);
        boolean hasTeacherType = Teacher.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginTeacherAnnotation && hasTeacherType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.equals("anonymousUser")) {
            return null;
        }

        try {
            TeacherDetails userDetails = (TeacherDetails) principal;

            return userDetails.getTeacher();
        } catch (ClassCastException e) {
            throw new CustomException(FORBIDDEN);
        }
    }
}
