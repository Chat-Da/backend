package site.chatda.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.chatda.global.argument_resolver.StudentLoginArgumentResolver;
import site.chatda.global.argument_resolver.TeacherLoginArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new StudentLoginArgumentResolver());
        resolvers.add(new TeacherLoginArgumentResolver());
    }
}
