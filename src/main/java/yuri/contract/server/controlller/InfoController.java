package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuri.contract.server.service.AuthenticationService;
import yuri.contract.server.util.response.ResponseFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(tags = "基础信息查询")
public class InfoController extends BaseController {
    private final AuthenticationService authenticationService;

    @Autowired
    public InfoController(HttpServletRequest request, AuthenticationService authenticationService) {
        super(request);
        this.authenticationService = authenticationService;
    }

    @ApiOperation("查询token对应的用户名")
    @CrossOrigin
    @GetMapping("/name")
    @ResponseBody
    public ResponseEntity<String> queryName() {
        return ResponseFactory.success(getOperator());
    }

    @ApiOperation("查询用户拥有的所有权限")
    @CrossOrigin
    @GetMapping("/functions")
    @ResponseBody
    public ResponseEntity<List<String>> queryFunctions() {
        return authenticationService.queryFunctions(getOperator());
    }
}
