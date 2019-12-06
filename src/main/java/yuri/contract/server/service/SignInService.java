package yuri.contract.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yuri.contract.server.mapper.ContractLogMapper;
import yuri.contract.server.mapper.UserMapper;
import yuri.contract.server.util.SecurityUtils;
import yuri.contract.server.util.response.ResponseFactory;

@Service
@Component
public class SignInService extends BaseService {
    private final UserMapper userMapper;

    @Autowired
    public SignInService(ContractLogMapper logMapper, UserMapper userMapper) {
        super(logMapper);
        this.userMapper = userMapper;
    }

    public ResponseEntity<String> signIn(String name, String password) {
        if (userMapper.select(name, password) != null) {
            return ResponseFactory.success(SecurityUtils.getToken(name));
        }
        return ResponseFactory.unauthorized(name);
    }

    public ResponseEntity<String> signUp(String name, String password) {
        if (userMapper.count(name) > 0) return ResponseFactory.badRequest(name + " exists");
        userMapper.insert(name, password);
        writeLog(name, "user created");
        return ResponseFactory.success(SecurityUtils.getToken(name));
    }

}
