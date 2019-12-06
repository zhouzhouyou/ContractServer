package yuri.contract.server.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yuri.contract.server.mapper.ContractLogMapper;
import yuri.contract.server.mapper.UserMapper;
import yuri.contract.server.model.User;
import yuri.contract.server.util.response.ResponseFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * User服务类
 */
@Service
@Component
public class UserService extends BaseService {
    private final UserMapper userMapper;

    @Autowired
    public UserService(ContractLogMapper logMapper, UserMapper userMapper) {
        super(logMapper);
        this.userMapper = userMapper;
    }

    /**
     * 是否存在该账户
     *
     * @param name     用户名
     * @return 是否存在该账户
     */
    public boolean exists(String name) {
        return userMapper.count(name) > 0;
    }

    public User findUserByName(String name) {
        return userMapper.findUserByName(name);
    }

    public ResponseEntity<String> deleteUserByName(String operator ,String name) {
        writeLog(operator, "delete user: " + name);
        return ResponseFactory.success(name);
    }

    public ResponseEntity<String> insertUser(String operator, User user) {
        int count = userMapper.insert(user.getName(), user.getPassword());
        if (count == 0) return ResponseFactory.badRequest(user.getName());
        writeLog(operator, "insert user: " + user.getName());
        return ResponseFactory.success(user.getName());
    }

    public ResponseEntity<List<String>> selectAll() {
        List<User> users = userMapper.selectAll();
        List<String> names = new ArrayList<>();
        users.forEach(user -> names.add(user.getName()));
        return ResponseFactory.success(names);
    }
}
