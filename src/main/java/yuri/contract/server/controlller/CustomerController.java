package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import yuri.contract.server.model.Customer;
import yuri.contract.server.service.CustomerService;
import yuri.contract.server.util.annotation.NeedToken;
import yuri.contract.server.util.response.ResponseFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@Api(tags = "客户管理")
public class CustomerController extends BaseController{
    private final CustomerService customerService;

    @Autowired
    public CustomerController(HttpServletRequest request, CustomerService customerService) {
        super(request);
        this.customerService = customerService;
    }

    @ApiOperation("模糊查询")
    @CrossOrigin
    @ResponseBody
    @PostMapping("/fuzzyQuery")
    @NeedToken(function = NeedToken.SELECT_CUSTOMER)
    public ResponseEntity<List<Customer>> fuzzyQuery(@RequestBody Query a, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(null);
        return customerService.fuzzyQuery(a.content);
    }

    @ApiOperation("查询所有的客户")
    @PostMapping("/selectAll")
    @CrossOrigin
    @ResponseBody
    @NeedToken(function = NeedToken.SELECT_CUSTOMER)
    public ResponseEntity<List<Customer>> queryAll() {
        return customerService.selectAll();
    }

    @ApiOperation("新增客户")
    @PutMapping("/insert")
    @CrossOrigin
    @ResponseBody
    @NeedToken(function = NeedToken.INSERT_CUSTOMER)
    public ResponseEntity<String> insert(@RequestBody Customer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return customerService.insert(customer, getOperator());
    }

    @ApiOperation("更新用户")
    @PutMapping("/update")
    @CrossOrigin
    @ResponseBody
    @NeedToken(function = NeedToken.UPDATE_CUSTOMER)
    public ResponseEntity<String> update(@RequestBody Customer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return customerService.update(customer, getOperator());
    }

    @ApiOperation("删除客户")
    @DeleteMapping("/delete")
    @CrossOrigin
    @ResponseBody
    @NeedToken(function = NeedToken.DELETE_CUSTOMER)
    public ResponseEntity<String> delete(@RequestBody Num num, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return customerService.delete(num.num, getOperator());
    }

    @Data
    @ApiModel
    private static class Query {
        private String content;
    }

    @Data
    @ApiModel
    private static class Num {
        private Integer num;
    }
}
