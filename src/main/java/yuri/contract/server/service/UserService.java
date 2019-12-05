package yuri.contract.server.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yuri.contract.server.mapper.UserMapper;
import yuri.contract.server.model.User;

/**
 * User服务类
 */
@Service
@Component
public class UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
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
}
