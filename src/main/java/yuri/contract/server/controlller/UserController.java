package yuri.contract.server.controlller;

import io.swagger.annotations.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import yuri.contract.server.model.User;
import yuri.contract.server.service.SignInService;
import yuri.contract.server.service.UserService;
import yuri.contract.server.util.annotation.NeedToken;
import yuri.contract.server.util.response.ResponseFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@Api(tags = "To Control user operations")
@RestController
@RequestMapping("/api")
public class UserController extends BaseController{
    private final SignInService loginService;
    private final UserService userService;

    @Autowired
    public UserController(HttpServletRequest request, SignInService loginService, UserService userService) {
        super(request);
        this.loginService = loginService;
        this.userService = userService;
    }

    @ApiOperation("用户登录，会返回token")
    @CrossOrigin
    @PostMapping(value = "/signIn")
    @ResponseBody
    public ResponseEntity<String> signIn(@RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return loginService.signIn(user.getName(), user.getPassword());
    }

    @ApiOperation("用户注册，会返回token")
    @CrossOrigin
    @PostMapping(value = "/signUp")
    @ResponseBody
    public ResponseEntity<String> signUp(@RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());

        return loginService.signUp(user.getName(), user.getPassword());
    }

    @ApiIgnore
    @CrossOrigin
    @RequestMapping("/test")
    @ResponseBody
    @NeedToken(function = NeedToken.DRAFT_CONTRACT)
    public ResponseEntity<String> test() {
        return ResponseFactory.success("test!");
    }


    @ApiOperation("删除用户")
    @CrossOrigin
    @DeleteMapping(value = "/user/delete")
    @ResponseBody
    @NeedToken(function = NeedToken.DELETE_USER)
    public ResponseEntity<String> deleteUser(@RequestBody Name name, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return userService.deleteUserByName(getOperator(), name.getName());
    }

    @ApiOperation("插入用户（这个不是注册，是直接插入，所以不会返回token）")
    @CrossOrigin
    @PutMapping(value = "/user/insert")
    @ResponseBody
    @NeedToken(function = NeedToken.INSERT_USER)
    public ResponseEntity<String> insertUser(@RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return userService.insertUser(getOperator(), user);
    }

    @ApiOperation("获取所有用户名, List<String>")
    @CrossOrigin
    @GetMapping(value = "/user/selectAll")
    @ResponseBody
    @NeedToken(function = NeedToken.SELECT_USER)
    public ResponseEntity<List<String>> selectAllUser() {
        return userService.selectAll();
    }

    @Data
    private static class Name {
        private String name;
    }

}
