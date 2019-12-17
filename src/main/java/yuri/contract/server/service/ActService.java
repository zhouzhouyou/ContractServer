package yuri.contract.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yuri.contract.server.controlller.ActController;
import yuri.contract.server.mapper.ActMapper;
import yuri.contract.server.mapper.ContractLogMapper;
import yuri.contract.server.model.Act;
import yuri.contract.server.model.ContractLog;
import yuri.contract.server.model.Role;
import yuri.contract.server.util.response.ResponseFactory;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Service
@Component
public class ActService extends BaseService {
    private final ActMapper actMapper;

    @Autowired
    public ActService(ContractLogMapper logMapper, ActMapper actMapper) {
        super(logMapper);
        this.actMapper = actMapper;
    }

    public ResponseEntity<List<Role>> findRoleByName(String username) {
        return ResponseFactory.success(actMapper.findRoleByName(username));
    }

    public ResponseEntity<List<Act>> fuzzyQuery(String query) {
        return ResponseFactory.success(actMapper.fuzzyQuery(query));
    }

    public ResponseEntity<String> update(String username, List<Integer> roles, String operator) {
        try {
            actMapper.deleteByUsername(username);
            roles.forEach(i -> actMapper.insert(username, i, ""));
            writeLog(operator, "修改" + username + "的角色");
            return ResponseFactory.success(username);
        }
        catch (Exception e) {
            return ResponseFactory.badRequest(e.toString());
        }
    }
}
