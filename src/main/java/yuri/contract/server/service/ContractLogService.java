package yuri.contract.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yuri.contract.server.mapper.ContractLogMapper;
import yuri.contract.server.model.ContractLog;
import yuri.contract.server.util.response.ResponseFactory;

import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import java.sql.Date;

@Service
@Component
public class ContractLogService extends BaseService{
    private  ContractLogMapper contractLogMapper;

    @Autowired
    public ContractLogService(ContractLogMapper logMapper,ContractLogMapper contractLogMapper) {
        super(logMapper);
        this.contractLogMapper = contractLogMapper;
    }

    public ResponseEntity<List<ContractLog>> selectAllLog() {
        return ResponseFactory.success(contractLogMapper.selectAll());
    }

    public ResponseEntity<List<ContractLog>> filterSelectLog(String userName,Date fromTime,Date toTime) {
        if(fromTime == null){
            fromTime = Date.valueOf("1900-01-01");
        }
        if (toTime == null){
            toTime = Date.valueOf("2100-01-01");
        }
        Timestamp begin = new Timestamp(fromTime.getTime());
        Timestamp end = new Timestamp(toTime.getTime() + 86400000);
        if(userName.equals("")){
            List<ContractLog> contractLogs = contractLogMapper.selectLogWithoutUserName(begin, end);
            return ResponseFactory.success(contractLogs);
        }else {
            List<ContractLog> contractLogs = contractLogMapper.selectLogWithUserName(userName,begin,end);
            return ResponseFactory.success(contractLogs);
        }
    }

    public ResponseEntity<List<ContractLog>> fuzzyQuery(String query) {
        return ResponseFactory.success(contractLogMapper.fuzzyQuery(query));
    }

    public List<ContractLog> getLogList(){
        return contractLogMapper.selectAll();
    }
}
