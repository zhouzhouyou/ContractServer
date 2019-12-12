package yuri.contract.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yuri.contract.server.mapper.ActMapper;
import yuri.contract.server.mapper.ContractLogMapper;
import yuri.contract.server.util.response.ResponseFactory;

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

    public ResponseEntity<String> update(String username, List<Integer> roles, String operator) {
        try {
            actMapper.deleteByUsername(username);
            for(var role : roles) actMapper.insert(username, role, "");
            writeLog(operator, "修改" + username + "的权限");
            return ResponseFactory.success(username);
        }
        catch (Exception e) {
            return ResponseFactory.badRequest(e.toString());
        }

    }
}
