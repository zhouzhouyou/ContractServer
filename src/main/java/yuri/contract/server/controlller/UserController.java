package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "控制用户相关")
@RestController
@RequestMapping("/api")
public class UserController extends BaseController {
    private final SignInService loginService;
    private final UserService userService;

    @Autowired
    public UserController(HttpServletRequest request, SignInService loginService, UserService userService) {
        super(request);
        this.loginService = loginService;
        this.userService = userService;
    }

    /**
     * 处理用户登录
     *
     * @param user          用户的信息
     * @param bindingResult 绑定结果
     * @return 登录成功返回token
     */
    @ApiOperation("用户登录，会返回token")
    @CrossOrigin
    @PostMapping(value = "/signIn")
    @ResponseBody
    public ResponseEntity<String> signIn(@RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return loginService.signIn(user.getName(), user.getPassword());
    }

    /**
     * 处理用户注册
     *
     * @param user          新用户信息
     * @param bindingResult 绑定结果
     * @return 注册成功返回token
     */
    @ApiOperation("用户注册，会返回token")
    @CrossOrigin
    @PostMapping(value = "/signUp")
    @ResponseBody
    public ResponseEntity<String> signUp(@RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());

        return loginService.signUp(user.getName(), user.getPassword());
    }

    /**
     * 测试，需要 {@link NeedToken#DRAFT_CONTRACT}
     *
     * @return 有起草合同的权限就会返回
     */
    @ApiIgnore
    @CrossOrigin
    @RequestMapping("/test")
    @ResponseBody
    @NeedToken(function = NeedToken.DRAFT_CONTRACT)
    public ResponseEntity<String> test() {
        return ResponseFactory.success("test!");
    }

    /**
     * 删除一个用户,需要{@link NeedToken#DELETE_USER}
     *
     * @param name          用户名
     * @param bindingResult 绑定结果
     * @return 删除用户是否成功
     */
    @ApiOperation("删除用户")
    @CrossOrigin
    @DeleteMapping(value = "/user/delete")
    @ResponseBody
    @NeedToken(function = NeedToken.DELETE_USER)
    public ResponseEntity<String> deleteUser(@RequestBody Name name, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return userService.deleteUserByName(getOperator(), name.getName());
    }

    /**
     * 插入用户，需要{@link NeedToken#INSERT_USER}
     *
     * @param user          用户信息
     * @param bindingResult 绑定结果
     * @return 用户名
     */
    @ApiOperation("插入用户（这个不是注册，是直接插入，所以不会返回token）")
    @CrossOrigin
    @PutMapping(value = "/user/insert")
    @ResponseBody
    @NeedToken(function = NeedToken.INSERT_USER)
    public ResponseEntity<String> insertUser(@RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return userService.insertUser(getOperator(), user);
    }

    @ApiOperation("当前可以分配任务的用户，顺序为会签，审核，签订")
    @CrossOrigin
    @PostMapping("/user/available")
    @ResponseBody
    @NeedToken(function = NeedToken.SELECT_USER)
    public ResponseEntity<List<List<String>>> queryAvailableUsers() {
        return userService.queryAvailableUsers();
    }

    /**
     * 获取所有用户名，需要 {@link NeedToken#SELECT_USER}
     *
     * @return 所有的用户名
     */
    @ApiOperation("获取所有用户名, List<String>")
    @CrossOrigin
    @GetMapping(value = "/user/selectAll")
    @ResponseBody
    @NeedToken(function = NeedToken.SELECT_USER)
    public ResponseEntity<List<String>> selectAllUser() {
        return userService.selectAll();
    }

    @ApiOperation("重置密码")
    @CrossOrigin
    @PostMapping(value = "/user/resetPassword")
    @ResponseBody
    public ResponseEntity<String> resetPassword(@RequestBody Password password, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        String operator = getOperator();
        return userService.resetPassword(operator, password.password);
    }

    @ApiOperation("重置密码")
    @CrossOrigin
    @PostMapping(value = "/user/resetOthersPassword")
    @ResponseBody
    public ResponseEntity<String> resetOthersPassword(@RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        String operator = getOperator();
        return userService.resetOthersPassword(operator, user.getName(), user.getPassword());
    }

    @ApiOperation("模糊查询")
    @CrossOrigin
    @PostMapping("/user/fuzzyQuery")
    @ResponseBody
    public ResponseEntity<List<String>> fuzzyQuery(@RequestBody Name name, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(null);
        return userService.fuzzyQuery(name.name);
    }

    @Data
    @ApiModel(description = "用户名")
    private static class Name {
        private String name;
    }

    @Data
    @ApiModel("密码")
    private static class Password {
        private String password;
    }
}
