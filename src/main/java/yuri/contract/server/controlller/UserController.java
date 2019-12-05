package yuri.contract.server.controlller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import yuri.contract.server.model.User;
import yuri.contract.server.service.LoginService;
import yuri.contract.server.service.UserService;
import yuri.contract.server.util.annotation.NeedToken;
import yuri.contract.server.util.response.ResponseFactory;

@RestController
@RequestMapping("/api")
public class UserController {
    private final LoginService loginService;
    private final UserService userService;

    @Autowired
    public UserController(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    @CrossOrigin
    @PostMapping(value = "/login", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> loginPost(@RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return loginService.login(user.getName(), user.getPassword());
    }

    @CrossOrigin
    @PostMapping(value = "/signIn")
    @ResponseBody
    public ResponseEntity<String> signIn(@RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return loginService.signIn(user.getName(), user.getPassword());
    }

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
