package yuri.contract.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yuri.contract.server.mapper.ContractLogMapper;
import yuri.contract.server.mapper.FunctionMapper;
import yuri.contract.server.mapper.RoleMapper;
import yuri.contract.server.util.response.ResponseFactory;

import java.util.List;

@Service
@Component
public class FuctionService extends BaseService {
    private final FunctionMapper functionMapper;

    @Autowired
    public FuctionService(ContractLogMapper logMapper, FunctionMapper functionMapper) {
        super(logMapper);
        this.functionMapper = functionMapper;
    }

    public ResponseEntity<List<String>> selectAll(String all) {
        return ResponseFactory.success(functionMapper.selectAllName());
    }
}
