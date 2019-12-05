package yuri.contract.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yuri.contract.server.mapper.ContractLogMapper;

import java.sql.Date;

@Slf4j
@Component
public abstract class BaseService {
    private final ContractLogMapper logMapper;

    @Autowired
    public BaseService(ContractLogMapper logMapper) {
        this.logMapper = logMapper;
    }

    protected void writeLog(String username, String content) {
        logMapper.insert(username, content, new Date(System.currentTimeMillis()));
        log.info("write log:{}", content);
    }
}
