package yuri.contract.server.controlller;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yuri.contract.server.util.SecurityUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public abstract class BaseController {
    private HttpServletRequest request;

    @Autowired
    public BaseController(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 获取token中对应的username
     *
     * @return 当前token属于哪一个user
     */
    protected String getOperator() {
        String token = request.getHeader("token");
        if (token == null) return null;

        Claims claims;
        try {
            claims = SecurityUtils.parseJWT(token);
        } catch (Exception e) {
            return null;
        }

        return claims.getSubject();
    }
}
