package yuri.contract.server.service;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.model.IProcessableElementTag;
import yuri.contract.server.mapper.*;
import yuri.contract.server.model.Contract;
import yuri.contract.server.model.ContractAttachment;
import yuri.contract.server.model.ContractProcess;
import yuri.contract.server.model.ContractState;
import yuri.contract.server.model.util.EnumValue;
import yuri.contract.server.model.util.OperationState;
import yuri.contract.server.model.util.OperationType;
import yuri.contract.server.model.util.Status;
import yuri.contract.server.util.response.ResponseFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.*;

import static yuri.contract.server.model.util.Status.COUNTER_SIGN_FINISHED;


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

    public ResponseEntity<String> addContract(String operator, Contract contract) {
        int count = contractMapper.insert(contract, operator);
        if (count == 0)
            return ResponseFactory.badRequest("fail to add");
        writeLog(operator, "add contract: " + contract.getName());
        int contractNum = contract.getNum();
        processMapper.insert(contractNum, OperationType.ASSIGN.getValue(), OperationState.UNFINISHED.getValue(), contract.getUserName(), contract.getContent());
        stateMapper.insert(contractNum, Status.DRAFT.getValue());
        String attachmentName = contract.getUserName();
        String type = attachmentName.substring(attachmentName.lastIndexOf(".") + 1);
        attachmentMapper.insert(contractNum, contract.getUserName(), "", type);
        return ResponseFactory.success(contract.getName());
    }

    public ResponseEntity<String> uploadFile(MultipartFile file) {
        String originName = file.getOriginalFilename();
        int count = attachmentMapper.getNewFileNameCount(originName);
        String fileType = originName.substring(originName.lastIndexOf(".") + 1);
        String newName = originName.substring(0, originName.lastIndexOf(".")) + "(" + count + ")" + fileType;
        File newFile = new File("./Attachment/" + newName);
        if (!newFile.getParentFile().exists()) newFile.getParentFile().mkdirs();
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseFactory.success(newName);
    }

//    public ResponseEntity<String> deleteContractByNum(String operator, String contractNum) {
//        int count = contractMapper.delete(contractNum);
//        if (count == 0)
//            return ResponseFactory.badRequest("fail to delete");
//        writeLog(operator, "delete contract: " + contractNum);
//        return ResponseFactory.success(contractNum);
//    }

    public ResponseEntity<Contract> selectContractByNum(int contractNum) {
        Contract getterContract = contractMapper.select(contractNum);
        if (getterContract == null)
            return ResponseFactory.badRequest(null);
        else
            return ResponseFactory.success(getterContract);
    }

    public ResponseEntity<List<Contract>> selectAllContracts() {
        List<Contract> contracts = contractMapper.selectAll();
        return ResponseFactory.success(contracts);
    }

    public ResponseEntity<String> addContractAttachment(String operator, ContractAttachment attachment) {
        int count = attachmentMapper.insert(attachment.getContractNum(), attachment.getFileName(), attachment.getPath(), attachment.getType());
        if (count == 0)
            return ResponseFactory.badRequest("fail to add contract attachment");
        writeLog(operator, "add contract attachment for " + attachment.getContractNum());
        return ResponseFactory.success("add contract attachment for " + attachment.getContractNum());
    }

    public ResponseEntity<List<Contract>> selectAllUnAssignedContracts() {
        List<Integer> contractNums = processMapper.selectNumOfUnAssigned();
        List<Contract> contracts = new ArrayList<>();
        contractNums.forEach(contractNum -> contracts.add(contractMapper.select(contractNum)));
        return ResponseFactory.success(contracts);
    }

    public ResponseEntity<String> doAssignJob(String operator, ContractProcess process) {
        int count = processMapper.insert(process.getContractNum(), process.getType().getValue(),
                process.getState().getValue(), process.getUserName(),
                process.getContent());
        if (count == 0)
            return ResponseFactory.badRequest("fail to assign");
        writeLog(operator, " assigned " + process.getContractNum() + " " + process.getType().getDesc() + " to " + process.getUserName());
        return ResponseFactory.success(process.getContractNum() + " " + process.getType().getDesc());
    }

    public ResponseEntity<List<Contract>> fuzzySelectAllUnAssignedContracts(String content) {
        List<Integer> contractNums = processMapper.fuzzySelectNumOfUnAssigned(content);
        List<Contract> contracts = new ArrayList<>();
        contractNums.forEach(contractNum -> contracts.add(contractMapper.select(contractNum)));
        return ResponseFactory.success(contracts);
    }

    public ResponseEntity<List<Contract>> selectAllNeededContracts(String operator, int type) {
        List<Integer> contractNums = processMapper.selectNumOfNeededProcess(operator, type);
        List<Contract> contracts = new ArrayList<>();
        contractNums.forEach(contractNum -> contracts.add(contractMapper.select(contractNum)));
        return ResponseFactory.success(contracts);
    }

    public ResponseEntity<List<Contract>> fuzzySelectAllNeededContracts(String operator, String content, int type) {
        List<Integer> contractNums = processMapper.fuzzySelectNumOfNeededProcess(operator, content, type);
        List<Contract> contracts = new ArrayList<>();
        contractNums.forEach(contractNum -> contracts.add(contractMapper.select(contractNum)));
        return ResponseFactory.success(contracts);
    }

    public ResponseEntity<String> doProcessJob(String operator, ContractProcess process, int type) {
        int count = processMapper.insert(process.getContractNum(), process.getType().getValue(),
                process.getState().getValue(), process.getUserName(),
                process.getContent());
        int createNumber = processMapper.getNumberOfNeededTypeState(type, OperationState.UNFINISHED.getValue());
        int finishNumber = processMapper.getNumberOfNeededTypeState(type, OperationState.FINISHED.getValue());
        if (createNumber == finishNumber) {
            switch (process.getType().getValue()) {
                case 1:
                    stateMapper.insert(process.getContractNum(), Status.COUNTER_SIGN_FINISHED.getValue());
                    break;
                case 2:
                    stateMapper.insert(process.getContractNum(), Status.FINALIZE_FINISHED.getValue());
                    break;
                case 3:
                    stateMapper.insert(process.getContractNum(), Status.REVIEW_FINISHED.getValue());
                    break;
                case 4:
                    stateMapper.insert(process.getContractNum(), Status.SIGN_FINISHED.getValue());
                    break;
                default:
                    break;
            }
        }
        if (count == 0)
            return ResponseFactory.badRequest("fail");
        writeLog(operator, " countersigned " + process.getContractNum());
        return ResponseFactory.success(process.getContractNum() + " " + process.getType().getDesc());
    }

    public ResponseEntity<String> getContractStatus(int contractNum) {
        int count = stateMapper.getContractStatus(contractNum);
        String result = "no such contract";
        switch (count) {
            case 1:
                result = "Drafted";
                break;
            case 2:
                result = "Finalized";
                break;
            case 3:
                result = "Reviewed";
                break;
            case 4:
                result = "Signed";
                break;
            default:
                break;
        }
        return ResponseFactory.success(result);
    }

    public ResponseEntity<String> deleteContract(String operator, int contractNum) {
        int count = contractMapper.delete(contractNum);
        if (count == 0)
            return ResponseFactory.badRequest("fail to delete contract.");
        writeLog(operator, "delete contract: " + contractNum);
        return ResponseFactory.success("delete contract: " + contractNum);
    }


//    public ResponseEntity<String> addContractProcess(String operator, ContractProcess process) {
//        int count = processMapper.insert(process.getContractNum(), process.getType().getValue(),
//                process.getState().getValue(), process.getUserName(),
//                process.getContent());
//        if (count == 0)
//            return ResponseFactory.badRequest("fail to add contract process");
//        writeLog(operator, "add contract process: " + process.getContractNum() + " " + process.getType().getDesc());
//        return ResponseFactory.success(process.getContractNum() + " " + process.getType().getDesc());
//    }

//    public ResponseEntity<String> deleteContractProcess(String operator, ContractProcess process) {
//        int count = processMapper.delete(process.getContractNum(), process.getType().getValue(), process.getUserName());
//        if (count == 0)
//            return ResponseFactory.badRequest("fail to delete contract process");
//        writeLog(operator, "delete contract process: " + process.getContractNum() + " " + process.getType().getDesc());
//        return ResponseFactory.success(process.getContractNum() + " " + process.getType().getDesc());
//    }
//
//    public ResponseEntity<String> updateContractProcessState(String operator, ContractProcess process) {
//        int count = processMapper.updateState(process.getState().getValue(), process.getContractNum(), process.getType().getValue(), process.getUserName());
//        if (count == 0)
//            return ResponseFactory.badRequest("fail to update contract process");
//        writeLog(operator, "update contract process: " + process.getContractNum() + " to " + process.getType().getDesc());
//        return ResponseFactory.success(process.getContractNum() + " to " + process.getType().getDesc());
//    }
//
//    public ResponseEntity<String> selectContractProcess(ContractProcess process) {
//        ContractProcess getterProcess = processMapper.select(process.getContractNum(), process.getType().getValue(), process.getUserName());
//        if (getterProcess == null)
//            return ResponseFactory.badRequest("no such process");
//        else
//            return ResponseFactory.success(getterProcess.getContractNum() + " " + getterProcess.getType().getDesc());
//    }
//
//    public ResponseEntity<String> addContractState(String operator, ContractState state) {
//        int count = stateMapper.insert(state.getNum(), state.getStatus().getValue());
//        if (count == 0)
//            return ResponseFactory.badRequest("fail to add contract state");
//        writeLog(operator, "add contract state: " + state.getNum() + " " + state.getStatus().getDesc());
//        return ResponseFactory.success(state.getNum() + " " + state.getStatus().getDesc());
//    }
//
//    public ResponseEntity<String> deleteContractState(String operator, ContractState state) {
//        int count = stateMapper.delete(state.getNum());
//        if (count == 0)
//            return ResponseFactory.badRequest("fail to delete state");
//        writeLog(operator, "delete contract state: " + state.getNum() + " " + state.getStatus().getDesc());
//        return ResponseFactory.success(state.getNum() + " " + state.getStatus().getDesc());
//    }

//    public ResponseEntity<String> updateContractStateStatus(String operator, ContractState state) {
//        int count = stateMapper.updateStatus(state.getNum(), state.getStatus().getValue());
//        if (count == 0)
//            return ResponseFactory.badRequest("fail to update state");
//        writeLog(operator, "update contract state: " + state.getNum() + " to " + state.getStatus().getDesc());
//        return ResponseFactory.success(state.getNum() + " to " + state.getStatus().getDesc());
//    }

//    public ResponseEntity<String> selectContractState(String contractNum) {
//        ContractState getterState = stateMapper.select(contractNum);
//        if (getterState == null)
//            return ResponseFactory.badRequest("no such contract state");
//        else
//            return ResponseFactory.success(getterState.getNum() + " " + getterState.getStatus().getDesc());
//    }


//    public ResponseEntity<String> deleteContractAttachment(String operator, String contractNum) {
//        int count = attachmentMapper.delete(contractNum);
//        if (count == 0)
//            return ResponseFactory.badRequest("fail to delete contract attachment");
//        writeLog(operator, "delete contract attachment for " + contractNum);
//        return ResponseFactory.success("delete contract attachment for " + contractNum);
//    }


}
