package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Api("提供网址位置")
@Controller
public class PageController {
    @GetMapping("login")
    public String login(HttpServletRequest request) {
        return "login";
    }

    @GetMapping("login.html")
    public String login2(HttpServletRequest request) {
        return "login";
    }

    @GetMapping("header.html")
    public String header(HttpServletRequest request) {
        return "header";
    }

    @GetMapping("welcome")
    public String welcome(HttpServletRequest request) {
        return "welcome";
    }

    @GetMapping("signUp")
    public String signUp(HttpServletRequest request) {
        return "signUp";
    }

    @GetMapping("signUp.html")
    public String signUp2(HttpServletRequest request) {
        return "signUp";
    }
}
