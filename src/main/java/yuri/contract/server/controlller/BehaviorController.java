package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import yuri.contract.server.service.AuthenticationService;
import yuri.contract.server.service.BaseService;
import yuri.contract.server.service.BehaviorService;
import yuri.contract.server.util.annotation.NeedToken;
import yuri.contract.server.util.response.ResponseFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "角色权限控制")
@RestController
@RequestMapping("/api/behavior")
public class BehaviorController extends BaseController {
    private final BehaviorService behaviorService;
    private final AuthenticationService authenticationService;

    @Autowired
    public BehaviorController(HttpServletRequest request, BehaviorService behaviorService, AuthenticationService authenticationService) {
        super(request);
        this.behaviorService = behaviorService;
        this.authenticationService = authenticationService;
    }

    @ApiOperation("更新角色权限")
    @CrossOrigin
    @PostMapping("/update")
    @ResponseBody
    @NeedToken(function = NeedToken.UPDATE_ROLE)
    public ResponseEntity<String> updateBehavior(@RequestBody BehaviorController.UpdateBehavior updateBehavior, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        }
        return behaviorService.update(updateBehavior.roleId, updateBehavior.nums, getOperator());
    }

    @ApiOperation("查询全部角色权限")
    @CrossOrigin
    @PostMapping("/selectAll")
    @ResponseBody
    @NeedToken(function = NeedToken.SELECT_ROLE)
    public ResponseEntity<List<String>> selectAll() {
        return behaviorService.selectAll();
    }

    @ApiOperation("查询某个角色的权限")
    @CrossOrigin
    @PostMapping("/selectFunctions")
    @ResponseBody
    @NeedToken(function = NeedToken.SELECT_ROLE)
    public ResponseEntity<List<String>> selectFunctionsByRole(@RequestBody RoleId roleId, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) return ResponseFactory.badRequest(null);
        return authenticationService.queryFunctionsByRoleId(roleId.id);
    }

    @Data
    private static class UpdateBehavior {
        private Integer roleId;
        private List<String> nums;
    }

    @Data
    @ApiModel
    private static class RoleId {
        private Integer id;
    }
}