package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("unused")
@Api(tags = "提供网址位置")
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

    @GetMapping("draft")
    public String draft(HttpServletRequest request) {
        return "draft";
    }

    @GetMapping("draft.html")
    public String draft2(HttpServletRequest request) {
        return "draft";
    }

    @GetMapping("contract")
    public String contract(HttpServletRequest request) {
        return "contract";
    }

    @GetMapping("contract.html")
    public String contract2(HttpServletRequest request) {
        return "contract";
    }

    @GetMapping("assign")
    public String assign(HttpServletRequest request) {
        return "assign";
    }

    @GetMapping("assign.html")
    public String assign2(HttpServletRequest request) {
        return "assign";
    }

    @GetMapping("countersign")
    public String countersign(HttpServletRequest request) {
        return "countersign";
    }

    @GetMapping("countersign.html")
    public String countersign2(HttpServletRequest request) {
        return "countersign";
    }

    @GetMapping("finalize")
    public String finalize(HttpServletRequest request) {
        return "finalize";
    }

    @GetMapping("finalize.html")
    public String finalize2(HttpServletRequest request) {
        return "finalize";
    }

    @GetMapping("review")
    public String review(HttpServletRequest request) {
        return "review";
    }

    @GetMapping("review.html")
    public String review2(HttpServletRequest request) {
        return "review";
    }

    @GetMapping("sign")
    public String sign(HttpServletRequest request) {
        return "sign";
    }

    @GetMapping("sign.html")
    public String sign2(HttpServletRequest request) {
        return "sign";
    }
}
