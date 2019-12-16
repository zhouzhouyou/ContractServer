package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import yuri.contract.server.model.Act;
import yuri.contract.server.service.ActService;
import yuri.contract.server.util.annotation.NeedToken;
import yuri.contract.server.util.response.ResponseFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "用户角色控制")
@RestController
@RequestMapping("/api")
public class ActController extends BaseController {
    private final ActService actService;

    @Autowired
    public ActController(HttpServletRequest request, ActService actService) {
        super(request);
        this.actService = actService;
    }

    @ApiOperation("更新用户角色")
    @CrossOrigin
    @PostMapping(value = "/act/update")
    @ResponseBody
    @NeedToken(function = NeedToken.UPDATE_USER)
    public ResponseEntity<String> updateAct(@RequestBody UpdateAct updateAct, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        }
        return actService.update(updateAct.username, updateAct.roles, getOperator());
    }

    @ApiOperation("查找用户角色")
    @CrossOrigin
    @PostMapping(value = "/act/selectAct")
    @ResponseBody
    @NeedToken(function = NeedToken.GRANT)
    public ResponseEntity<List<Act>> sendQuery(@RequestBody SelectAct selectAct, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseFactory.badRequest(null);
        }
        return actService.findRoleByName(selectAct.username, getOperator());
    }

    @ApiOperation("模糊查找用户角色")
    @CrossOrigin
    @PostMapping(value = "/act/fuzzyQuery")
    @ResponseBody
    @NeedToken(function = NeedToken.GRANT)
    public ResponseEntity<List<Act>> sendFuzzyQuery(@RequestBody Query query, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseFactory.badRequest(null);
        }
        return actService.fuzzyQuery(query.query);
    }

    @ApiOperation("查找指定用户角色")
    @CrossOrigin
    @PostMapping(value = "/act/filterSelect")
    @ResponseBody
    @NeedToken(function = NeedToken.GRANT)
    public ResponseEntity<List<Act>> sendfilterSelect(@RequestBody SelectAct selectAct, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseFactory.badRequest(null);
        }
        return actService.filterSelectAct(selectAct.username);
    }



    @Data
    @ApiModel
    private static class UpdateAct {
        private String username;
        private List<String> roles;
    }

    @Data
    @ApiModel(description = "模糊查询请求")
    private static class Query {
        /**
         * 关键字
         */
        private String query;
    }

    @Data
    @ApiModel
    private static class SelectAct {
        private String username;
    }

}
