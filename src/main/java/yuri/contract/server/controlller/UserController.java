package yuri.contract.server.controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuri.contract.server.service.UserService;
import yuri.contract.server.util.response.ResponseFactory;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 登录用api: ?name=%s&password=%s
     *
     * @param name     用户名
     * @param password 密码
     * @return 登录成功返回 {@link org.springframework.http.HttpStatus#OK},
     *         失败返回 {@link org.springframework.http.HttpStatus#UNAUTHORIZED},
     *         附带的信息均为用户名
     */
    @CrossOrigin
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> loginPost(@RequestParam("name") String name,
                                            @RequestParam("password") String password) {
        return userService.exists(name, password);
    }
}
