package yuri.contract.server.service;

import io.swagger.models.auth.In;
import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.model.IProcessableElementTag;
import yuri.contract.server.mapper.*;
import yuri.contract.server.model.*;
import yuri.contract.server.model.util.EnumValue;
import yuri.contract.server.model.util.OperationState;
import yuri.contract.server.model.util.OperationType;
import yuri.contract.server.model.util.Status;
import yuri.contract.server.util.response.ResponseFactory;
import yuri.contract.server.model.DetailContractMessage.Message;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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
        processMapper.insert(contractNum, OperationType.ASSIGN.getValue(), OperationState.UNFINISHED.getValue(), operator, contract.getContent());
        processMapper.insert(contractNum, OperationType.FINALIZE.getValue(), OperationState.UNFINISHED.getValue(), operator, contract.getContent());
        stateMapper.insert(contractNum, Status.DRAFT.getValue());
        String attachmentName = contract.getUserName();
        String type = attachmentName.substring(attachmentName.lastIndexOf(".") + 1);
        attachmentMapper.insert(contractNum, attachmentName, "", type);
        return ResponseFactory.success(contract.getName());
    }

    public ResponseEntity<String> uploadFile(MultipartFile file) {
        String basePath = System.getProperty("user.dir") + "/attachment";
        String fullName = file.getOriginalFilename();
        String fileName = fullName.substring(0, fullName.lastIndexOf("."));
        String fileType = fullName.substring(fullName.lastIndexOf(".") + 1);

        File descFile = new File(basePath + File.separator + fullName);
        int count = 1;
        while (descFile.exists()) {
            String newName = fileName + "(" + count + ")." + fileType;
            descFile = new File(basePath + File.separator + newName);
            count++;
        }
        if (!descFile.getParentFile().exists()) descFile.getParentFile().mkdirs();
        try {
            file.transferTo(descFile);
        } catch (IOException e) {
            return ResponseFactory.badRequest(e.toString());
        }
        return ResponseFactory.success(String.format("{\"finalName\":\"%s\"}", descFile.getName()));
    }

//    public ResponseEntity<String> deleteContractByNum(String operator, String contractNum) {
//        int count = contractMapper.delete(contractNum);
//        if (count == 0)
//            return ResponseFactory.badRequest("fail to delete");
//        writeLog(operator, "delete contract: " + contractNum);
//        return ResponseFactory.success(contractNum);
//    }

    private Contract selectContractByNum(int contractNum) {
        return contractMapper.select(contractNum);
    }

    private List<Contract> selectAllContracts() {
        return contractMapper.selectAll();
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
        if (contractNums == null || contractNums.size() == 0)
            return ResponseFactory.success(contracts);
        contractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) >= 2);
        contractNums.forEach(contractNum -> contracts.add(contractMapper.select(contractNum)));
        return ResponseFactory.success(contracts);
    }

//    public ResponseEntity<String> doAssignJob(String operator, ContractProcess process) {
//        int count = processMapper.insert(process.getContractNum(), OperationType.ASSIGN.getValue(),
//                OperationState.FINISHED.getValue(), process.getUserName(),
//                process.getContent());
//        if (count == 0)
//            return ResponseFactory.badRequest("fail to assign");
//        stateMapper.insert(process.getContractNum(), Status.ASSIGN.getValue());
//        writeLog(operator, " assigned " + process.getContractNum() + " " + process.getType().getDesc() + " to " + process.getUserName());
//        return ResponseFactory.success(process.getContractNum() + " " + process.getType().getDesc());
//    }

    public ResponseEntity<String> doAssignJob(String operator, List<List<String>> lists, int contractNum) {
        var countersigns = lists.get(0);
        var reviews = lists.get(1);
        var signs = lists.get(2);

        countersigns.forEach(countersignUser -> {
            processMapper.insert(contractNum, OperationType.COUNTER_SIGH.getValue(),
                    OperationState.UNFINISHED.getValue(), countersignUser, "");
            processMapper.insert(contractNum, OperationType.ASSIGN.getValue(),
                    OperationState.FINISHED.getValue(), countersignUser, "");
            stateMapper.insert(contractNum, Status.ASSIGN.getValue());
            writeLog(operator, "assigned " + countersignUser + " to countersign" + contractNum + " contract");
        });

        reviews.forEach(reviewUser -> {
            processMapper.insert(contractNum, OperationType.REVIEW.getValue(),
                    OperationState.UNFINISHED.getValue(), reviewUser, "");
            processMapper.insert(contractNum, OperationType.ASSIGN.getValue(),
                    OperationState.FINISHED.getValue(), reviewUser, "");
            stateMapper.insert(contractNum, Status.ASSIGN.getValue());
            writeLog(operator, "assigned " + reviewUser + " to review" + contractNum + " contract");
        });

        signs.forEach(signUser -> {
            processMapper.insert(contractNum, OperationType.SIGN.getValue(),
                    OperationState.UNFINISHED.getValue(), signUser, "");
            processMapper.insert(contractNum, OperationType.ASSIGN.getValue(),
                    OperationState.FINISHED.getValue(), signUser, "");
            stateMapper.insert(contractNum, Status.ASSIGN.getValue());
            writeLog(operator, "assigned " + signUser + " to sign" + contractNum + " contract");
        });

        return ResponseFactory.success("assign job done.");

    }

    public ResponseEntity<List<Contract>> fuzzySelectAllUnAssignedContracts(String content) {
        List<Integer> contractNums = processMapper.fuzzySelectNumOfUnAssigned(content);
        List<Contract> contracts = new ArrayList<>();
        if (contractNums == null || contractNums.size() == 0)
            return ResponseFactory.success(contracts);
        contractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) >= 2);
        contractNums.forEach(contractNum -> contracts.add(contractMapper.select(contractNum)));
        return ResponseFactory.success(contracts);
    }

    public ResponseEntity<List<Contract>> selectAllNeededContracts(String operator, int type) {
        List<Integer> contractNums = processMapper.selectNumOfNeededProcess(operator, type);
        List<Contract> contracts = new ArrayList<>();
        if (contractNums == null || contractNums.size() == 0)
            return ResponseFactory.success(contracts);
        switch (type) {
            case 0:
                contractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 1);
                break;
            case 1:
                contractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 2);
                break;
            case 2:
                contractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 3);
                break;
            case 3:
                contractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 4);
                break;
            default:
                break;
        }
        contractNums.forEach(contractNum -> contracts.add(contractMapper.select(contractNum)));
        return ResponseFactory.success(contracts);
    }

    public ResponseEntity<List<Contract>> fuzzySelectAllNeededContracts(String operator, String content, int type) {
        List<Integer> contractNums = processMapper.fuzzySelectNumOfNeededProcess(operator, content, type);
        List<Contract> contracts = new ArrayList<>();
        if (contractNums == null || contractNums.size() == 0)
            return ResponseFactory.success(contracts);
        switch (type) {
            case 0:
                contractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 1);
                break;
            case 1:
                contractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 2);
                break;
            case 2:
                contractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 3);
                break;
            case 3:
                contractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 4);
                break;
            default:
                break;
        }
        contractNums.forEach(contractNum -> contracts.add(contractMapper.select(contractNum)));
        return ResponseFactory.success(contracts);
    }

    public ResponseEntity<String> doProcessJob(String operator, ContractProcess process, int type) {
        int count = processMapper.insert(process.getContractNum(), process.getType().getValue(),
                process.getState().getValue(), operator,
                process.getContent());
        if (count == 0)
            return ResponseFactory.badRequest("fail");
        int createNumber = processMapper.getNumberOfNeededTypeState(type, OperationState.UNFINISHED.getValue());
        int finishNumber = processMapper.getNumberOfNeededTypeState(type, OperationState.FINISHED.getValue());
        if (createNumber == finishNumber) {
            switch (process.getType().getValue()) {
                case 0:
                    stateMapper.insert(process.getContractNum(), Status.COUNTER_SIGN_FINISHED.getValue());
                    break;
                case 1:
                    stateMapper.insert(process.getContractNum(), Status.FINALIZE_FINISHED.getValue());
                    break;
                case 2:
                    stateMapper.insert(process.getContractNum(), Status.REVIEW_FINISHED.getValue());
                    break;
                case 3:
                    stateMapper.insert(process.getContractNum(), Status.SIGN_FINISHED.getValue());
                    break;
                default:
                    break;
            }
        }
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
                result = "Assigned";
                break;
            case 3:
                result = "Countersigned";
                break;
            case 4:
                result = "Finalized";
                break;
            case 5:
                result = "Reviewed";
                break;
            case 6:
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

    public ResponseEntity<List<Boolean>> hasJobsToDo(String operator) {
        List<Boolean> list = new ArrayList<>(Collections.nCopies(6, false));
        if (!selectAllNeededContracts(operator, 1).getBody().isEmpty()) {
            list.set(1, true);
            list.set(0, true);
        }
        if (!selectAllNeededContracts(operator, 0).getBody().isEmpty()) {
            list.set(2, true);
            list.set(0, true);
        }
        if (!selectAllNeededContracts(operator, 2).getBody().isEmpty()) {
            list.set(3, true);
            list.set(0, true);
        }
        if (!selectAllNeededContracts(operator, 3).getBody().isEmpty()) {
            list.set(4, true);
            list.set(0, true);
        }
        if (!selectAllUnAssignedContracts().getBody().isEmpty()) {
            list.set(5, true);
            list.set(0, true);
        }
        return ResponseFactory.success(list);

    }

    public ResponseEntity<List<ContractWithState>> selectAllContractsWithState() {
        List<Contract> contracts = selectAllContracts();
        List<ContractWithState> contractWithStates = new ArrayList<>();
        if (contracts == null || contracts.size() == 0) {
            return ResponseFactory.success(contractWithStates);
        }

        contracts.forEach(contract -> contractWithStates.add(new ContractWithState(contract, getContractState(contract.getNum()))));
        return ResponseFactory.success(contractWithStates);
    }

    public ResponseEntity<List<ContractWithState>> fuzzyQueryAllContract(String content) {
        List<Contract> contracts = contractMapper.fuzzyQuery(content);
        List<ContractWithState> contractWithStates = new ArrayList<>();
        if (contracts == null || contracts.size() == 0) {
            return ResponseFactory.success(contractWithStates);
        }

        contracts.forEach(contract -> contractWithStates.add(new ContractWithState(contract, getContractState(contract.getNum()))));
        return ResponseFactory.success(contractWithStates);
    }

    public ResponseEntity<List<ContractWithState>> filterQueryAllContract(boolean[] statuses, Integer customerNum) {
        List<Contract> contracts = selectAllContracts();
        List<ContractWithState> contractWithStates = new ArrayList<>();
        if (customerNum != null) {
            contracts.removeIf(contract -> !contract.getCustomerNum().equals(customerNum));
        }
        for (int i = 0; i < statuses.length; i++) {
            if (!statuses[i]) {
                var iterator = contracts.iterator();
                while (iterator.hasNext()) {
                    Contract contract = iterator.next();
                    if (stateMapper.getContractStatus(contract.getNum()) == i + 1)
                        iterator.remove();
                }
            }
        }

        if (contracts == null || contracts.size() == 0) {
            return ResponseFactory.success(contractWithStates);
        }

        contracts.forEach(contract -> contractWithStates.add(new ContractWithState(contract, getContractState(contract.getNum()))));
        return ResponseFactory.success(contractWithStates);

    }

    private String getContractState(int contractNum) {
        int count = stateMapper.getContractStatus(contractNum);
        String result = null;
        switch (count) {
            case 1:
                result = "已起草";
                break;
            case 2:
                result = "已分配";
                break;
            case 3:
                result = "已会签";
                break;
            case 4:
                result = "已定稿";
                break;
            case 5:
                result = "已审核";
                break;
            case 6:
                result = "已签订";
                break;
            default:
                break;
        }
        return result;
    }

    public ResponseEntity<DetailContractMessage> getDetailContractMessage(String operator, int contractNum) {
        List<List<Message>> lists = new ArrayList<>();
        Contract contract = selectContractByNum(contractNum);
        List<Message> drafter = new ArrayList<>();
        List<Message> finalizer = new ArrayList<>();
        List<Message> assigner = new ArrayList<>();
        List<Message> counterSigners = new ArrayList<>();
        List<Message> reviewers = new ArrayList<>();
        List<Message> signers = new ArrayList<>();
        lists.add(drafter);
        lists.add(assigner);
        lists.add(finalizer);
        lists.add(counterSigners);
        lists.add(reviewers);
        lists.add(signers);

        drafter.add(new Message(contract.getName(), "起草", "完成"));
        List<String> assignOperator = processMapper.selectOperator(contractNum, -1);
        if (stateMapper.getContractStatus(contractNum) == 1) {
            assignOperator.forEach(assign -> assigner.add(new Message(assign, "分配", "未完成")));
        } else if (stateMapper.getContractStatus(contractNum) == 2) {
            List<String> countersignOperator = processMapper.selectOperator(contractNum, 0);
            List<String> finalizeOperator = processMapper.selectOperator(contractNum, 1);
            List<String> reviewOperator = processMapper.selectOperator(contractNum, 2);
            List<String> signOperator = processMapper.selectOperator(contractNum, 3);
            assignOperator.forEach(assign -> assigner.add(new Message(assign, "分配", "已完成")));
            countersignOperator.forEach(countersign -> counterSigners.add(new Message( countersign, "会签", "未完成")));
            finalizeOperator.forEach(finalize -> finalizer.add(new Message(finalize, "定稿", "未完成")));
            reviewOperator.forEach(review ->reviewers.add(new Message(review,"审核","未完成")));
            signOperator.forEach(sign->signers.add(new Message(sign,"签订","未完成")));

        } else if (stateMapper.getContractStatus(contractNum) == 3) {
            List<String> countersignOperator = processMapper.selectOperator(contractNum, 0);
            List<String> finalizeOperator = processMapper.selectOperator(contractNum, 1);
            List<String> reviewOperator = processMapper.selectOperator(contractNum, 2);
            List<String> signOperator = processMapper.selectOperator(contractNum, 3);
            assignOperator.forEach(assign -> assigner.add(new Message(assign, "分配", "已完成")));
            countersignOperator.forEach(countersign -> counterSigners.add(new Message(countersign, "会签", "已完成")));
            finalizeOperator.forEach(finalize -> finalizer.add(new Message(finalize, "定稿", "未完成")));
            reviewOperator.forEach(review ->reviewers.add(new Message(review,"审核","未完成")));
            signOperator.forEach(sign->signers.add(new Message(sign,"签订","未完成")));
        } else if (stateMapper.getContractStatus(contractNum) == 4) {
            List<String> countersignOperator = processMapper.selectOperator(contractNum, 0);
            List<String> finalizeOperator = processMapper.selectOperator(contractNum, 1);
            List<String> reviewOperator = processMapper.selectOperator(contractNum, 2);
            List<String> signOperator = processMapper.selectOperator(contractNum, 3);
            assignOperator.forEach(assign -> assigner.add(new Message(assign, "分配", "已完成")));
            countersignOperator.forEach(countersign -> counterSigners.add(new Message(countersign, "会签", "已完成")));
            finalizeOperator.forEach(finalize -> finalizer.add(new Message(finalize, "定稿", "已完成")));
            reviewOperator.forEach(review ->reviewers.add(new Message(review,"审核","未完成")));
            signOperator.forEach(sign->signers.add(new Message(sign,"签订","未完成")));
        } else if (stateMapper.getContractStatus(contractNum) == 5) {
            List<String> countersignOperator = processMapper.selectOperator(contractNum, 0);
            List<String> finalizeOperator = processMapper.selectOperator(contractNum, 1);
            List<String> reviewOperator = processMapper.selectOperator(contractNum, 2);
            List<String> signOperator = processMapper.selectOperator(contractNum, 3);
            assignOperator.forEach(assign -> assigner.add(new Message(assign, "分配", "已完成")));
            countersignOperator.forEach(countersign -> counterSigners.add(new Message(countersign, "会签", "已完成")));
            finalizeOperator.forEach(finalize -> finalizer.add(new Message(finalize, "定稿", "已完成")));
            reviewOperator.forEach(review ->reviewers.add(new Message(review,"审核","已完成")));
            signOperator.forEach(sign->signers.add(new Message(sign,"签订","未完成")));
        } else if (stateMapper.getContractStatus(contractNum) == 6) {
            List<String> countersignOperator = processMapper.selectOperator(contractNum, 0);
            List<String> finalizeOperator = processMapper.selectOperator(contractNum, 1);
            List<String> reviewOperator = processMapper.selectOperator(contractNum, 2);
            List<String> signOperator = processMapper.selectOperator(contractNum, 3);
            assignOperator.forEach(assign -> assigner.add(new Message(assign, "分配", "已完成")));
            countersignOperator.forEach(countersign -> counterSigners.add(new Message( countersign, "会签", "已完成")));
            finalizeOperator.forEach(finalize -> finalizer.add(new Message(finalize, "定稿", "已完成")));
            reviewOperator.forEach(review ->reviewers.add(new Message(review,"审核","已完成")));
            signOperator.forEach(sign->signers.add(new Message(sign,"签订","已完成")));
        }
        DetailContractMessage detailContractMessage = new DetailContractMessage(contract,lists);
        return ResponseFactory.success(detailContractMessage);
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
