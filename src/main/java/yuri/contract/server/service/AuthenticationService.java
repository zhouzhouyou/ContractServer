package yuri.contract.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yuri.contract.server.mapper.ActMapper;
import yuri.contract.server.mapper.BehaviorMapper;
import yuri.contract.server.model.Behavior;
import yuri.contract.server.model.User;

import java.util.List;

@Service
@Component
public class AuthenticationService {
    private final BehaviorMapper behaviorMapper;
    private final ActMapper actMapper;

    @Autowired
    public AuthenticationService(BehaviorMapper behaviorMapper, ActMapper actMapper) {
        this.behaviorMapper = behaviorMapper;
        this.actMapper = actMapper;
    }

    public boolean hasFunction(User user, String function) {
        List<String> roles = actMapper.FindByUsername(user.getName());
        for (var role : roles) {
            Behavior behavior = behaviorMapper.select(role, function);
            if (behavior != null) return true;
        }
        return false;
    }
}