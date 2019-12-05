package yuri.contract.server.configurer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import yuri.contract.server.interceptor.AuthenticationInterceptor;

@Configuration
public class InterceptorConfigurer implements WebMvcConfigurer {
    private final AuthenticationInterceptor interceptor;

    @Autowired
    public InterceptorConfigurer(AuthenticationInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**");
    }
}
