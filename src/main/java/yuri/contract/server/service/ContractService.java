package yuri.contract.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yuri.contract.server.mapper.*;
import yuri.contract.server.model.Contract;
import yuri.contract.server.util.response.ResponseFactory;


@Service
@Component
public class ContractService extends BaseService {
    private final ContractMapper contractMapper;
    private final ContractProcessMapper processMapper;
    private final ContractStateMapper stateMapper;
    private final ContractAttachmentMapper attachmentMapper;

    @Autowired
    public ContractService(ContractLogMapper logMapper,
                           ContractMapper contractMapper,
                           ContractProcessMapper processMapper,
                           ContractStateMapper stateMapper,
                           ContractAttachmentMapper attachmentMapper) {
        super(logMapper);
        this.contractMapper = contractMapper;
        this.processMapper = processMapper;
        this.stateMapper = stateMapper;
        this.attachmentMapper = attachmentMapper;
    }

    public ResponseEntity<String> addContract(Contract contract) {
        int count = contractMapper.insert(contract.getNum(), contract.getName(), contract.getCustomer(),
                contract.getBegin(), contract.getEnd(), contract.getContent(), contract.getUserName());
        return count == 0 ?
                ResponseFactory.badRequest("fail to insert") :
                ResponseFactory.success(contract.getName());
    }
}
