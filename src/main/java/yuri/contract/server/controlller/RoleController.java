package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import yuri.contract.server.service.ActService;
import yuri.contract.server.service.RoleService;
import yuri.contract.server.util.annotation.NeedToken;
import yuri.contract.server.util.response.ResponseFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "用户角色查找")
@RestController
@RequestMapping("/api")
public class RoleController extends BaseController {
    private final RoleService roleService;

    @Autowired
    public RoleController(HttpServletRequest request, RoleService roleService) {
        super(request);
        this.roleService = roleService;
    }

    @ApiOperation("查找全部角色")
    @CrossOrigin
    @PostMapping(value = "/role/selectAll")
    @ResponseBody
    @NeedToken(function = NeedToken.UPDATE_USER)
    public ResponseEntity<List<String>> selectAll() {
        return roleService.selectAll("all");
    }
}
