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
public class CustomerService extends BaseService {
    private final CustomerMapper customerMapper;

    public CustomerService(ContractLogMapper logMapper, CustomerMapper customerMapper) {
        super(logMapper);
        this.customerMapper = customerMapper;
    }

    /**
     * 获取所有客户
     *
     * @return 所有客户
     */
    public ResponseEntity<List<Customer>> selectAll() {
        return ResponseFactory.success(customerMapper.selectAll());
    }

    /**
     * 插入一个新客户
     *
     * @param customer 客户
     * @param operator 操作员
     * @return 插入客户的结果
     */
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

    /**
     * 更新客户信息
     *
     * @param customer 新客户的信息
     * @param operator 操作者
     * @return 更新客户的结果
     */
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

    /**
     * 模糊查询一个客户
     *
     * @param a 模糊查询的目标
     * @return 客户
     */
    public ResponseEntity<List<Customer>> fuzzyQuery(String a) {
        return ResponseFactory.success(customerMapper.fuzzyQuery(a));
    }

    /**
     * 删除一个客户
     *
     * @param num      客户的序号
     * @param operator 操作者
     * @return 删除客户的结果
     */
    public ResponseEntity<String> delete(Integer num, String operator) {
        int flag = customerMapper.delete(num);
        if (flag == 0) return ResponseFactory.badRequest("");
        writeLog(operator, "删除了一个客户，客户编号为" + num);
        return ResponseFactory.success(String.valueOf(num));
    }
}
