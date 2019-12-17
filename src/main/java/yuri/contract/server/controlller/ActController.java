package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import yuri.contract.server.model.Act;
import yuri.contract.server.model.Role;
import yuri.contract.server.model.User;
import yuri.contract.server.service.ActService;
import yuri.contract.server.util.annotation.NeedToken;
import yuri.contract.server.util.response.ResponseFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "用户角色控制")
@RestController
@RequestMapping("/api/act")
public class ActController extends BaseController {
    private final ActService actService;

    @Autowired
    public ActController(HttpServletRequest request, ActService actService) {
        super(request);
        this.actService = actService;
    }

    @ApiOperation("更新用户角色")
    @CrossOrigin
    @PostMapping("/update")
    @ResponseBody
    @NeedToken(function = NeedToken.UPDATE_USER)
    public ResponseEntity<String> updateAct(@RequestBody UpdateAct updateAct, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return actService.update(updateAct.username, updateAct.ids, getOperator());
    }

    @ApiOperation("查找用户角色")
    @CrossOrigin
    @PostMapping("/select")
    @ResponseBody
    @NeedToken(function = NeedToken.GRANT)
    public ResponseEntity<List<Role>> selectUserRoles(@RequestBody UserName userName, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) return ResponseFactory.badRequest(null);
        return actService.findRoleByName(userName.username);
    }

    @ApiOperation("模糊查找用户角色")
    @CrossOrigin
    @PostMapping("/fuzzyQuery")
    @ResponseBody
    @NeedToken(function = NeedToken.GRANT)
    public ResponseEntity<List<Act>> sendFuzzyQuery(@RequestBody Query query, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseFactory.badRequest(null);
        }
        return actService.fuzzyQuery(query.content);
    }


    @Data
    @ApiModel
    private static class UpdateAct {
        private String username;
        private List<Integer> ids;
    }

    @Data
    @ApiModel(description = "模糊查询请求")
    private static class Query {
        /**
         * 关键字
         */
        private String content;
    }

    @Data
    @ApiModel
    private static class UserName {
        private String username;
    }
}
