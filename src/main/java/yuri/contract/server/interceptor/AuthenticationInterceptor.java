package yuri.contract.server.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import yuri.contract.server.model.User;
import yuri.contract.server.service.AuthenticationService;
import yuri.contract.server.service.UserService;
import yuri.contract.server.util.SecurityUtils;
import yuri.contract.server.util.annotation.NeedToken;
import yuri.contract.server.util.annotation.PassToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationInterceptor(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) return true;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //如果有PassToken这个注解而且pass为true，就通过
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.pass()) return true;
        }

        //如果没有NeedToken这个注解就通过
        if (!method.isAnnotationPresent(NeedToken.class)) return true;

        NeedToken needToken = method.getAnnotation(NeedToken.class);
        if (!needToken.required()) return true;

        String token = request.getHeader("token");
        if (token == null) throw new RuntimeException("No token, please add token in your header");

        Claims claims;
        try {
            claims = SecurityUtils.parseJWT(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String username = claims.getSubject();

        User user = userService.findUserByName(username);
        if (user == null) throw new RuntimeException("No username: " + username);

        String function = needToken.function();
        if (!authenticationService.hasFunction(user, function)) throw new RuntimeException("No Permission");
        return true;
    }

}
