package yuri.contract.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import yuri.contract.server.mapper.UserMapper;
import yuri.contract.server.model.User;
import yuri.contract.server.util.response.ResponseFactory;

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
     * 是否存在某个账户（用户判断登录信息）
     *
     * @param name     用户名
     * @param password 密码
     * @return 是否存在该账户
     */
    public ResponseEntity<String> exists(String name, String password) {
        User user = userMapper.select(name, password);
        return user == null ? ResponseFactory.unauthorized(name) : ResponseFactory.success(name);
    }
}
