package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import yuri.contract.server.model.Role;
import yuri.contract.server.service.ActService;
import yuri.contract.server.service.RoleService;
import yuri.contract.server.util.annotation.NeedToken;
import yuri.contract.server.util.response.ResponseFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "角色")
@RestController
@RequestMapping("/api/role")
public class RoleController extends BaseController {
    private final RoleService roleService;

    @Autowired
    public RoleController(HttpServletRequest request, RoleService roleService) {
        super(request);
        this.roleService = roleService;
    }

    @ApiOperation("查找全部角色")
    @CrossOrigin
    @PostMapping("/selectAll")
    @ResponseBody
    @NeedToken(function = NeedToken.SELECT_ROLE)
    public ResponseEntity<List<Role>> selectAll() {
        return roleService.selectAll();
    }

    @ApiOperation("查找全部角色")
    @CrossOrigin
    @PostMapping("/fuzzyQuery")
    @ResponseBody
    @NeedToken(function = NeedToken.SELECT_ROLE)
    public ResponseEntity<List<Role>> selectAll(@RequestBody Query query, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(null);
        return roleService.fuzzyQuery(query.content);
    }

    @ApiOperation("新增角色")
    @CrossOrigin
    @PostMapping("/insert")
    @ResponseBody
    @NeedToken(function = NeedToken.INSERT_ROLE)
    public ResponseEntity<Integer> insert(@RequestBody Role role, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(null);
        return roleService.insert(getOperator(), role);
    }

    @ApiOperation("删除角色")
    @CrossOrigin
    @DeleteMapping("/delete")
    @ResponseBody
    @NeedToken(function = NeedToken.DELETE_ROLE)
    public ResponseEntity<Integer> delete(@RequestBody RoleId roleId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(null);
        return roleService.delete(getOperator(), roleId.id);
    }

    @Data
    @ApiModel
    private static class RoleId {
        private Integer id;
    }

    @Data
    @ApiModel(description = "模糊查询请求")
    private static class Query {
        /**
         * 关键字
         */
        private String content;
    }
}
