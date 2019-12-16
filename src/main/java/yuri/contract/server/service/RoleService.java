package yuri.contract.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yuri.contract.server.mapper.ActMapper;
import yuri.contract.server.mapper.ContractLogMapper;
import yuri.contract.server.mapper.RoleMapper;
import yuri.contract.server.model.Act;
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

    public ResponseEntity<List<String>> selectAll(String all) {
        return ResponseFactory.success(roleMapper.selectAllName());
    }
}
