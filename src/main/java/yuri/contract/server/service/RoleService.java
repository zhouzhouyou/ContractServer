package yuri.contract.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yuri.contract.server.mapper.ActMapper;
import yuri.contract.server.mapper.ContractLogMapper;
import yuri.contract.server.mapper.RoleMapper;
import yuri.contract.server.model.Act;
import yuri.contract.server.model.Role;
import yuri.contract.server.util.response.ResponseFactory;

import java.util.List;

@Service
@Component
public class RoleService extends BaseService  {
    private final RoleMapper roleMapper;

    @Autowired
    public RoleService(ContractLogMapper logMapper, RoleMapper roleMapper) {
        super(logMapper);
        this.roleMapper = roleMapper;
    }

    public ResponseEntity<List<Role>> selectAll() {
        return ResponseFactory.success(roleMapper.selectAll());
    }

    public ResponseEntity<List<Role>> fuzzyQuery(String content) {
        return ResponseFactory.success(roleMapper.fuzzyQuery(content));
    }

    public ResponseEntity<Integer> insert(String operator,  Role role) {
        int count = roleMapper.insert(role.getName(), role.getDescription());
        if (count == 0) return ResponseFactory.badRequest(null);
        writeLog(operator, "创建了" + role.getName());
        return ResponseFactory.success(count);
    }

    public ResponseEntity<Integer> delete(String operator, Integer id) {
        int count = roleMapper.delete(id);
        if (count == 0) return ResponseFactory.badRequest(null);
        writeLog(operator, "删除了角色，角色id：" + id);
        return ResponseFactory.success(id);
    }
}
