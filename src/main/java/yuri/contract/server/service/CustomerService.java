package yuri.contract.server.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yuri.contract.server.mapper.ContractLogMapper;
import yuri.contract.server.mapper.CustomerMapper;
import yuri.contract.server.model.Customer;
import yuri.contract.server.util.response.ResponseFactory;

import java.util.List;

@Service
@Component
public class CustomerService extends BaseService{
    private final CustomerMapper customerMapper;

    public CustomerService(ContractLogMapper logMapper, CustomerMapper customerMapper) {
        super(logMapper);
        this.customerMapper = customerMapper;
    }

    public ResponseEntity<List<Customer>> selectAll() {
        return ResponseFactory.success(customerMapper.selectAll());
    }

    public ResponseEntity<String> insert(Customer customer, String operator) {
        int flag = customerMapper.insert(
                customer.getName(),
                customer.getAddress(),
                customer.getTel(),
                customer.getFax(),
                customer.getCode(),
                customer.getBank(),
                customer.getAccount(),
                customer.getOther());
        if (flag == 0) return ResponseFactory.badRequest("");
        writeLog(operator, " 新增了一个客户，客户名为" + customer.getName());
        return ResponseFactory.success("");
    }

    public ResponseEntity<String> update(Customer customer, String operator) {
        int flag = customerMapper.update(
                customer.getNum(),
                customer.getName(),
                customer.getAddress(),
                customer.getTel(),
                customer.getFax(),
                customer.getCode(),
                customer.getBank(),
                customer.getAccount(),
                customer.getOther()
        );
        if (flag == 0) return ResponseFactory.badRequest("");
        writeLog(operator, " 更新了一个客户，客户名为" + customer.getName());
        return ResponseFactory.success("");
    }

    public ResponseEntity<List<Customer>> fuzzyQuery(String a) {
        return ResponseFactory.success(customerMapper.fuzzyQuery(a));
    }

    public ResponseEntity<String> delete(Integer num, String operator) {
        int flag = customerMapper.delete(num);
        if (flag == 0) return ResponseFactory.badRequest("");
        writeLog(operator, "删除了一个客户，客户编号为" + num);
        return ResponseFactory.success(String.valueOf(num));
    }
}
