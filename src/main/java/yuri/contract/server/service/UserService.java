package yuri.contract.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yuri.contract.server.mapper.*;
import yuri.contract.server.model.User;
import yuri.contract.server.util.response.ResponseFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User服务类
 */
@Service
@Component
public class UserService extends BaseService {
    private final UserMapper userMapper;
    private final BehaviorMapper behaviorMapper;
    private final ActMapper actMapper;
    private final ContractProcessMapper processMapper;

    @Autowired
    public UserService(ContractLogMapper logMapper, UserMapper userMapper, BehaviorMapper behaviorMapper, ActMapper actMapper, ContractProcessMapper processMapper) {
        super(logMapper);
        this.userMapper = userMapper;
        this.behaviorMapper = behaviorMapper;
        this.actMapper = actMapper;
        this.processMapper = processMapper;
    }

    /**
     * 是否存在该账户
     *
     * @param name 用户名
     * @return 是否存在该账户
     */
    public boolean exists(String name) {
        return userMapper.count(name) > 0;
    }

    /**
     * 通过用户名来查找用户
     *
     * @param name 用户名
     * @return 用户
     */
    public User findUserByName(String name) {
        return userMapper.findUserByName(name);
    }

    /**
     * 通过用户名来删除某个用户
     *
     * @param operator 操作者
     * @param name     用户名
     * @return 删除是否成功
     */
    public ResponseEntity<String> deleteUserByName(String operator, String name) {
        if (operator.equals(name)) return ResponseFactory.badRequest("你居然想删除自己？");
        if (processMapper.hasAnyJob(name) != 0) return ResponseFactory.badRequest("还有任务呢");
        int count = userMapper.delete(name);
        if (count == 0) return ResponseFactory.badRequest("删除失败");
        writeLog(operator, "删除了用户: " + name);
        return ResponseFactory.success(name);
    }

    /**
     * 创建一个新用户
     *
     * @param operator 创建人
     * @param user     创建的新账户
     * @return 创建是否成功
     */
    public ResponseEntity<String> insertUser(String operator, User user) {
        int count = userMapper.insert(user.getName(), user.getPassword());
        if (count == 0) return ResponseFactory.badRequest(user.getName());
        writeLog(operator, "创建了账户: " + user.getName());
        return ResponseFactory.success(user.getName());
    }

    /**
     * 获取所有用户的用户名
     *
     * @return 所有的用户名
     */
    public ResponseEntity<List<String>> selectAll() {
        List<User> users = userMapper.selectAll();
        List<String> names = new ArrayList<>();
        users.forEach(user -> names.add(user.getName()));
        return ResponseFactory.success(names);
    }

    /**
     * 获取可以会签、审核、签订的用户名
     *
     * @return 可以会签、审核、签订的用户名
     */
    public ResponseEntity<List<List<String>>> queryAvailableUsers() {
        List<List<String>> result = new ArrayList<>();

        Set<String> counterSignUsers = new HashSet<>();
        behaviorMapper.selectByNum("4").forEach(integer -> counterSignUsers.addAll(actMapper.selectByRole(integer)));

        Set<String> reviewUsers = new HashSet<>();
        behaviorMapper.selectByNum("5").forEach(integer -> reviewUsers.addAll(actMapper.selectByRole(integer)));

        Set<String> signUsers = new HashSet<>();
        behaviorMapper.selectByNum("6").forEach(integer -> signUsers.addAll(actMapper.selectByRole(integer)));

        result.add(new ArrayList<>(counterSignUsers));
        result.add(new ArrayList<>(reviewUsers));
        result.add(new ArrayList<>(signUsers));
        return ResponseFactory.success(result);
    }

    /**
     * 重置密码
     *
     * @param operator 谁在重置密码
     * @param password 密码
     * @return 重置密码的结果
     */
    public ResponseEntity<String> resetPassword(String operator, String password) {
        userMapper.updatePassword(operator, password);
        writeLog(operator, "重置了密码");
        return ResponseFactory.success("success");
    }

    /**
     * 管理员重置他人的密码
     *
     * @param operator 操作员
     * @param username 被重置密码的人
     * @param password 密码
     * @return 重置他人密码的结果
     */
    public ResponseEntity<String> resetOthersPassword(String operator, String username, String password) {
        userMapper.updatePassword(username, password);
        writeLog(operator, "重置了" + username + "的密码");
        return ResponseFactory.success(username);
    }

    /**
     * 模糊查询用户姓名
     *
     * @param name 用户姓名
     * @return 含有这个姓名的列表
     */
    public ResponseEntity<List<String>> fuzzyQuery(String name) {
        return ResponseFactory.success(userMapper.fuzzyQuery(name));
    }
}
