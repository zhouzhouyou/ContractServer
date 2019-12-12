package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import yuri.contract.server.service.BaseService;
import yuri.contract.server.service.BehaviorService;
import yuri.contract.server.util.annotation.NeedToken;
import yuri.contract.server.util.response.ResponseFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "角色权限控制")
@RestController
@RequestMapping("/api")
public class BehaviorController extends BaseController {
    private final BehaviorService behaviorService;

    @Autowired
    public BehaviorController(HttpServletRequest request, BehaviorService behaviorService) {
        super(request);
        this.behaviorService = behaviorService;
    }

    @ApiOperation("更新角色权限")
    @CrossOrigin
    @PostMapping(value = "/behavior/update")
    @ResponseBody
    @NeedToken(function = NeedToken.GRANT)
    public ResponseEntity<String> updateBehavior(@RequestBody BehaviorController.UpdateBehavior updateBehavior, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        }
        return behaviorService.update(updateBehavior.roleId, updateBehavior.nums, getOperator());
    }



    @Data
    private static class UpdateBehavior {
        private Integer roleId;
        private List<String> nums;
    }


}