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

    public ResponseEntity<List<Act>> findRoleByName(String username, String operator) {

        if(username.equals("***")){
            List<Act> roleName = actMapper.selectAll();
            writeLog(operator, "查找全部用户的角色");
            return ResponseFactory.success(roleName);
        }else {
            List<Act> roleName = actMapper.selectByUser(username);
            writeLog(operator, "查找" + username + "的角色");
            return ResponseFactory.success(roleName);
        }
    }

    public ResponseEntity<List<Act>> fuzzyQuery(String query) {
        return ResponseFactory.success(actMapper.fuzzyQuery(query));
    }

    public ResponseEntity<String> update(String username, List<String> roles, String operator) {
        try {
            actMapper.deleteByUsername(username);
            System.out.println("@@@@@1");
            for(var role : roles) actMapper.insert(username, role, "");
            System.out.println("@@@@@2");
            writeLog(operator, "修改" + username + "的权限");
            return ResponseFactory.success(username);
        }
        catch (Exception e) {
            return ResponseFactory.badRequest(e.toString());
        }
    }

    public ResponseEntity<List<Act>> filterSelectAct(String username) {

        if(username.equals("")){
            return ResponseFactory.success(actMapper.selectAll());
        }else {
            return ResponseFactory.success(actMapper.selectByUser(username));
        }
    }
}
