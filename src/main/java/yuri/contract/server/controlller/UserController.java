package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "To Control user operations")
@RestController
@RequestMapping("/api")
public class UserController {
    private final SignInService loginService;
    private final UserService userService;

    @Autowired
    public UserController(UserService userService, SignInService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    @ApiOperation("Handle sign in")
    @CrossOrigin
    @PostMapping(value = "/signIn", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> loginPost(@RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return loginService.signIn(user.getName(), user.getPassword());
    }

    @ApiOperation("Handle sign up")
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

//    @CrossOrigin
//    @DeleteMapping(value = "/delete")
//    @ResponseBody
//    public ResponseEntity<String> deleteUser(@RequestBody DeleteUserModel deleteUserModel, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
//        String token = deleteUserModel.getToken();
//        if ()
//    }



}
