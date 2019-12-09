package yuri.contract.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yuri.contract.server.mapper.ActMapper;
import yuri.contract.server.mapper.BehaviorMapper;
import yuri.contract.server.mapper.FunctionMapper;
import yuri.contract.server.model.Behavior;
import yuri.contract.server.model.Function;
import yuri.contract.server.model.User;
import yuri.contract.server.util.response.ResponseFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Component
public class AuthenticationService {
    private final FunctionMapper functionMapper;
    private final BehaviorMapper behaviorMapper;
    private final ActMapper actMapper;

    @Autowired
    public AuthenticationService(FunctionMapper functionMapper, BehaviorMapper behaviorMapper, ActMapper actMapper) {
        this.functionMapper = functionMapper;
        this.behaviorMapper = behaviorMapper;
        this.actMapper = actMapper;
    }

    /**
     * 通过用户名来判断是否拥有某个权限
     *
     * @param user     用户
     * @param function 权限
     * @return 是否拥有该权限
     */
    public boolean hasFunction(User user, String function) {
        List<Integer> roles = actMapper.FindByUsername(user.getName());
        for (var role : roles) {
            Behavior behavior = behaviorMapper.select(role, function);
            if (behavior != null) return true;
        }
        return false;
    }

    /**
     * 通过用户名来判断有哪些权限
     * @param username 用户名
     * @return 权限名列表
     */
    public ResponseEntity<List<String>> queryFunctions(String username) {
        List<Integer> roles = actMapper.FindByUsername(username);
        Set<String> functions = new HashSet<>();
        for (var role : roles) {
            functions.addAll(behaviorMapper.selectByRole(role));
        }
        List<String> functionNames = new ArrayList<>();
        for (var function : functions) {
            Function f  = functionMapper.select(function);
            functionNames.add(f.getName());
        }

        return ResponseFactory.success(functionNames);
    }
}
