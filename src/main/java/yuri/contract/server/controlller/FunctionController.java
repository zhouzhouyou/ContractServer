package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuri.contract.server.service.FuctionService;
import yuri.contract.server.util.annotation.NeedToken;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "角色权限查找")
@RestController
@RequestMapping("/api")
public class FunctionController extends BaseController {
    private final FuctionService fuctionService;

    @Autowired
    public FunctionController(HttpServletRequest request, FuctionService fuctionService) {
        super(request);
        this.fuctionService = fuctionService;
    }

    @ApiOperation("查找全部权限")
    @CrossOrigin
    @PostMapping(value = "/function/selectAll")
    @ResponseBody
    @NeedToken(function = NeedToken.UPDATE_USER)
    public ResponseEntity<List<String>> selectAll() {
        return fuctionService.selectAll("all");
    }
}
