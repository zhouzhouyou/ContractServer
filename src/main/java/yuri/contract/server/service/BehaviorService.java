package yuri.contract.server.service;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yuri.contract.server.mapper.ActMapper;
import yuri.contract.server.mapper.BehaviorMapper;
import yuri.contract.server.mapper.ContractLogMapper;
import yuri.contract.server.util.response.ResponseFactory;

import java.util.List;


@Service
@Component
public class BehaviorService extends BaseService {
    private final BehaviorMapper behaviorMapper;

    @Autowired

    public BehaviorService(ContractLogMapper logMapper, BehaviorMapper behaviorMapper) {
        super(logMapper);
        this.behaviorMapper = behaviorMapper;
    }

    public ResponseEntity<String> update(Integer roleId, List<String> nums, String operator) {
        try {
            behaviorMapper.deleteByRole(roleId);
            for(var num : nums) behaviorMapper.insert(roleId, num);
            writeLog(operator, "修改" + roleId + "角色所拥有的权限");
            return ResponseFactory.success(roleId.toString());
        }
        catch (Exception e) {
            return ResponseFactory.badRequest(e.toString());
        }
    }
}
