package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import yuri.contract.server.mapper.CustomerMapper;
import yuri.contract.server.model.Customer;
import yuri.contract.server.service.CustomerService;
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

    @ApiOperation("查询所有的客户")
    @GetMapping("/selectAll")
    @CrossOrigin
    @ResponseBody
    public ResponseEntity<List<Customer>> queryAll() {
        return customerService.selectAll();
    }

    @ApiOperation("新增客户")
    @GetMapping("/insert")
    @CrossOrigin
    @ResponseBody
    public ResponseEntity<String> insert(@RequestBody Customer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return customerService.insert(customer);
    }
}
