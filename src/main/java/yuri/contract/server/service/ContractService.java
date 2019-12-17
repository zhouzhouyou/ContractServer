package yuri.contract.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yuri.contract.server.mapper.*;
import yuri.contract.server.model.*;
import yuri.contract.server.model.DetailContractMessage.Message;
import yuri.contract.server.model.PreviousProcessMessage.PreviousMessage;
import yuri.contract.server.model.util.OperationState;
import yuri.contract.server.model.util.OperationType;
import yuri.contract.server.model.util.Status;
import yuri.contract.server.util.response.ResponseFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
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
        writeLog(operator, "起草了合同: " + contract.getName());
        int contractNum = contract.getNum();
        processMapper.insert(contractNum, OperationType.ASSIGN.getValue(), OperationState.UNFINISHED.getValue(), operator, contract.getContent());
        processMapper.insert(contractNum, OperationType.FINALIZE.getValue(), OperationState.UNFINISHED.getValue(), operator, contract.getContent());
        stateMapper.insert(contractNum, Status.DRAFT.getValue());
        String attachmentName = contract.getUserName();
        if (attachmentName != null) {
            String type = attachmentName.substring(attachmentName.lastIndexOf(".") + 1);
            attachmentMapper.insert(contractNum, attachmentName, "", type);
        }

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

    public ResponseEntity<String> downloadFile(int contractNum){
        String basePath = System.getProperty("user.dir") + "/attachment";
        String attachmentName = attachmentMapper.getAttachmentName(contractNum);
       // String type = attachmentMapper.getAttachmentType(contractNum);
        if(attachmentName == null||attachmentName.length() == 0)
            return ResponseFactory.success(null);
        File originFile = new File(basePath + File.separator + attachmentName);
        if(!originFile.exists())
            return ResponseFactory.success(null);
        //MultipartFile file = new MockMultipartFile(attachmentName,new FileInputStream(originFile));
        return ResponseFactory.success(originFile.getName());

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
        writeLog(operator, "添加附件 " + attachment.getContractNum());
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
        log.info("开始分配");
        var countersigns = lists.get(0);
        var reviews = lists.get(1);
        var signs = lists.get(2);

        countersigns.forEach(countersignUser -> {
            processMapper.insert(contractNum, OperationType.COUNTER_SIGH.getValue(),
                    OperationState.UNFINISHED.getValue(), countersignUser, "");
//            processMapper.insert(contractNum, OperationType.ASSIGN.getValue(),
//                    OperationState.FINISHED.getValue(), countersignUser, "");
            processMapper.updateState(OperationState.FINISHED.getValue(), contractNum,
                    OperationType.ASSIGN.getValue(), operator);
            writeLog(operator, "分配了 " + countersignUser + " 来会签 " + contractNum + " 合同");
        });
        log.info("会签分配");
        reviews.forEach(reviewUser -> {
            processMapper.insert(contractNum, OperationType.REVIEW.getValue(),
                    OperationState.UNFINISHED.getValue(), reviewUser, "");
//            processMapper.insert(contractNum, OperationType.ASSIGN.getValue(),
//                    OperationState.FINISHED.getValue(), reviewUser, "");
            processMapper.updateState(OperationState.FINISHED.getValue(), contractNum,
                    OperationType.ASSIGN.getValue(), operator);
            writeLog(operator, "分配了 " + reviewUser + " 来审核 " + contractNum + " 合同");
        });
        log.info("审核分配");
        signs.forEach(signUser -> {
            processMapper.insert(contractNum, OperationType.SIGN.getValue(),
                    OperationState.UNFINISHED.getValue(), signUser, "");
//            processMapper.insert(contractNum, OperationType.ASSIGN.getValue(),
//                    OperationState.FINISHED.getValue(), signUser, "");
            processMapper.updateState(OperationState.FINISHED.getValue(), contractNum,
                    OperationType.ASSIGN.getValue(), operator);
            writeLog(operator, "分配了 " + signUser + " 来签订 " + contractNum + " 合同");
        });
        log.info("签订分配");
        stateMapper.insert(contractNum, Status.ASSIGN.getValue());
        writeLog(operator,"完成了分配工作");
        return ResponseFactory.success("assign job done.");

    }

    public ResponseEntity<List<Contract>> fuzzySelectAllUnAssignedContracts(String content) {
        List<Contract> contracts = contractMapper.fuzzyQuery(content);
        List<Integer> contractNums = processMapper.selectNumOfUnAssigned();
        var iterator = contracts.iterator();
        Contract contract = null;
        while (iterator.hasNext()) {
            contract = iterator.next();
            boolean flag = false;
            for (var contractNum : contractNums) {
                if (contract.getNum() == contractNum) {
                    flag = true;
                    break;
                }
            }
            if (!flag)
                iterator.remove();
        }
        return ResponseFactory.success(contracts);
    }

    public ResponseEntity<List<Contract>> selectAllNeededContracts(String operator, int type) {
        List<Integer> unfinishedContractNums = processMapper.selectUnfinishedContractNum(operator, type, OperationState.UNFINISHED.getValue());
        //List<Integer> finishedContractNums = processMapper.selectUnfinishedContractNum(operator, type, OperationState.FINISHED.getValue());
        List<Contract> contracts = new ArrayList<>();
        if (unfinishedContractNums == null || unfinishedContractNums.size() == 0)
            return ResponseFactory.success(contracts);
        switch (type) {
            case 0:
                unfinishedContractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 2);
                break;
            case 1:
                unfinishedContractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 3);
                break;
            case 2:
                unfinishedContractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 4);
                break;
            case 3:
                unfinishedContractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 5);
                break;
            default:
                break;
        }
//        contractNums.forEach(contractNum -> contracts.add(contractMapper.select(contractNum)));
        unfinishedContractNums.forEach(contractNum -> contracts.add(contractMapper.select(contractNum)));
        return ResponseFactory.success(contracts);
    }

    public ResponseEntity<List<Contract>> fuzzySelectAllNeededContracts(String operator, String content, int type) {
        List<Integer> contractNums = processMapper.selectUnfinishedContractNum(operator, type, OperationState.UNFINISHED.getValue());
        //List<Integer> finishedContractNums = processMapper.fuzzySelectNumOfNeededProcess(operator, content, type, OperationState.FINISHED.getValue());

        List<Contract> contracts = contractMapper.fuzzyQuery(content);
        switch (type) {
            case 0:
                contractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 2);
                break;
            case 1:
                contractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 3);
                break;
            case 2:
                contractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 4);
                break;
            case 3:
                contractNums.removeIf(contractNum -> stateMapper.getContractStatus(contractNum) != 5);
                break;
            default:
                break;
        }
        var iterator = contracts.iterator();
        Contract contract = null;
        while (iterator.hasNext()) {
            contract = iterator.next();
            boolean flag = false;
            for (var contractNum : contractNums) {
                if (contract.getNum() == contractNum) {
                    flag = true;
                    break;
                }
            }
            if (!flag)
                iterator.remove();
        }
        return ResponseFactory.success(contracts);

    }

    public ResponseEntity<String> doProcessJob(String operator, int contractNum, String content, int type, int state) {
//        int count = processMapper.insert(contractNum, type,
//                state, operator,
//                content);
        int count = processMapper.updateState(state, contractNum, type, operator);
        processMapper.updateContent(content, contractNum, type, operator);
        if (count == 0)
            return ResponseFactory.badRequest("fail");
        switch (type) {
            case 0:
                writeLog(operator, "会签了合同: " + contractNum);
                break;
            case 1:
                writeLog(operator, "定稿了合同: " + contractNum);
                break;
            case 2:
                writeLog(operator, "审核了合同: " + contractNum);
                break;
            case 3:
                writeLog(operator, "签订了合同: " + contractNum);
                break;
            default:
                break;
        }
//        int createNumber = processMapper.getNumberOfNeededTypeState(type, OperationState.UNFINISHED.getValue());
//        int finishNumber = processMapper.getNumberOfNeededTypeState(type, OperationState.FINISHED.getValue());
        int unfinishedCount = processMapper.getUnfinishedOrDeniedProcess(type, contractNum);
        if (unfinishedCount == 0) {
            switch (type) {
                case 0:
                    stateMapper.insert(contractNum, Status.COUNTER_SIGN_FINISHED.getValue());
                    writeLog(operator, "会签合同: " + contractNum + " 完成.");
                    break;
                case 1:
                    stateMapper.insert(contractNum, Status.FINALIZE_FINISHED.getValue());
                    contractMapper.updateContent(contractNum, content);
                    writeLog(operator, "定稿合同: " + contractNum + " 完成.");
                    break;
                case 2:
                    stateMapper.insert(contractNum, Status.REVIEW_FINISHED.getValue());
                    writeLog(operator, "审核合同: " + contractNum + " 完成.");
                    break;
                case 3:
                    stateMapper.insert(contractNum, Status.SIGN_FINISHED.getValue());
                    writeLog(operator, "签订合同: " + contractNum + " 完成.");
                    break;
                default:
                    break;
            }
        }
        return ResponseFactory.success("Job finished");
    }

    public ResponseEntity<String> getContractStatus(int contractNum) {
        int count = stateMapper.getContractStatus(contractNum);
        String result = "no such contract";
        switch (count) {
            case 1:
                result = "Drafted";//未分配
                break;
            case 2:
                result = "Assigned";//未会签
                break;
            case 3:
                result = "Countersigned";//未定稿
                break;
            case 4:
                result = "Finalized";//未审核
                break;
            case 5:
                result = "Reviewed";//未签订
                break;
            case 6:
                result = "Signed";//未结束
                break;
            default:
                break;
        }
        return ResponseFactory.success(result);
    }

    public ResponseEntity<String> deleteContract(String operator, int contractNum) {
        int count = contractMapper.delete(contractNum);
        String basePath = System.getProperty("user.dir") + "/attachment";
        String attachmentName = attachmentMapper.getAttachmentName(contractNum);
        File file = new File(basePath + File.separator + attachmentName);
        file.delete();

        if (count == 0)
            return ResponseFactory.badRequest("fail to delete contract.");
        writeLog(operator, "删除合同: " + contractNum);
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
        for (int i = 0; i < statuses.length - 1; i++) {
            if (!statuses[i]) {
                var iterator = contracts.iterator();
                while (iterator.hasNext()) {
                    Contract contract = iterator.next();
                    if (stateMapper.getContractStatus(contract.getNum()) == i + 1)
                        iterator.remove();
                }
            }
        }

        if (!statuses[6]) {
            List<Integer> rejectedContractNums = processMapper.selectRejectContractNums();
            var iterator = contracts.iterator();
            while (iterator.hasNext()) {
                Contract contract = iterator.next();
                for (var rejectNum : rejectedContractNums) {
                    if (contract.getCustomerNum().equals(rejectNum))
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
                if (processMapper.getDenyCount(contractNum) != 0)
                    result = "已否决";
                else
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
        //List<Message> drafter = new ArrayList<>();
        List<Message> finalizer = new ArrayList<>();
        //List<Message> assigner = new ArrayList<>();
        List<Message> counterSigners = new ArrayList<>();
        List<Message> reviewers = new ArrayList<>();
        List<Message> signers = new ArrayList<>();
        //lists.add(drafter);
        //lists.add(assigner);
        lists.add(counterSigners);
        lists.add(finalizer);
        lists.add(reviewers);
        lists.add(signers);

        //drafter.add(new Message(contract.getName(), "起草", "完成"));
        //List<String> assignOperator = processMapper.selectOperator(contractNum, -1);
//        if (stateMapper.getContractStatus(contractNum) == 1) {
//            assignOperator.forEach(assign -> assigner.add(new Message(assign, "分配", "未完成")));}
        if (stateMapper.getContractStatus(contractNum) == 2) {
            //List<String> countersignOperator = processMapper.selectOperator(contractNum, 0);
            List<String> finishCountersign = processMapper.selectOperatorWithState(contractNum,0,1);
            List<String> unfinishedCountersign = processMapper.selectOperatorWithState(contractNum,0,0);
            List<String> finalizeOperator = processMapper.selectOperator(contractNum, 1);
            List<String> reviewOperator = processMapper.selectOperator(contractNum, 2);
            List<String> signOperator = processMapper.selectOperator(contractNum, 3);
            //assignOperator.forEach(assign -> assigner.add(new Message(assign, "分配", "已完成")));
            finishCountersign.forEach(countersign -> counterSigners.add(new Message(countersign, "会签", "已完成")));
            unfinishedCountersign.forEach(countersign -> counterSigners.add(new Message(countersign, "会签", "未完成")));
            finalizeOperator.forEach(finalize -> finalizer.add(new Message(finalize, "定稿", "未完成")));
            reviewOperator.forEach(review -> reviewers.add(new Message(review, "审核", "未完成")));
            signOperator.forEach(sign -> signers.add(new Message(sign, "签订", "未完成")));

        } else if (stateMapper.getContractStatus(contractNum) == 3) {
            List<String> countersignOperator = processMapper.selectOperator(contractNum, 0);
            List<String> finalizeOperator = processMapper.selectOperator(contractNum, 1);
            List<String> reviewOperator = processMapper.selectOperator(contractNum, 2);
            List<String> signOperator = processMapper.selectOperator(contractNum, 3);
            //assignOperator.forEach(assign -> assigner.add(new Message(assign, "分配", "已完成")));
            countersignOperator.forEach(countersign -> counterSigners.add(new Message(countersign, "会签", "已完成")));
            finalizeOperator.forEach(finalize -> finalizer.add(new Message(finalize, "定稿", "未完成")));
            reviewOperator.forEach(review -> reviewers.add(new Message(review, "审核", "未完成")));
            signOperator.forEach(sign -> signers.add(new Message(sign, "签订", "未完成")));
        } else if (stateMapper.getContractStatus(contractNum) == 4) {
            if (processMapper.getDenyCount(contractNum) == 0) {
                List<String> countersignOperator = processMapper.selectOperator(contractNum, 0);
                List<String> finalizeOperator = processMapper.selectOperator(contractNum, 1);
                //List<String> reviewOperator = processMapper.selectOperator(contractNum, 2);
                List<String> finishReview = processMapper.selectOperatorWithState(contractNum,2,1);
                List<String> unfinishedReview = processMapper.selectOperatorWithState(contractNum,2,0);
                List<String> signOperator = processMapper.selectOperator(contractNum, 3);
                //assignOperator.forEach(assign -> assigner.add(new Message(assign, "分配", "已完成")));
                countersignOperator.forEach(countersign -> counterSigners.add(new Message(countersign, "会签", "已完成")));
                finalizeOperator.forEach(finalize -> finalizer.add(new Message(finalize, "定稿", "已完成")));
                finishReview.forEach(review -> reviewers.add(new Message(review, "审核", "已完成")));
                unfinishedReview.forEach(review -> reviewers.add(new Message(review, "审核", "未完成")));
                signOperator.forEach(sign -> signers.add(new Message(sign, "签订", "未完成")));
            } else {
                List<String> countersignOperator = processMapper.selectOperator(contractNum, 0);
                List<String> finalizeOperator = processMapper.selectOperator(contractNum, 1);
                List<String> reviewOperator = processMapper.selectOperator(contractNum, 2);
                List<String> rejectOperator = processMapper.rejectOperator(contractNum);
                countersignOperator.forEach(countersign -> counterSigners.add(new Message(countersign, "会签", "已完成")));
                finalizeOperator.forEach(finalize -> finalizer.add(new Message(finalize, "定稿", "已完成")));
                for (var review : reviewOperator) {
                    for (var reject : rejectOperator)
                        if (review.equals(reject))
                            reviewers.add(new Message(review, "审核", "已否决"));
                        else
                            reviewers.add(new Message(review, "审核", "未完成"));
                }
            }
        } else if (stateMapper.getContractStatus(contractNum) == 5) {
            List<String> countersignOperator = processMapper.selectOperator(contractNum, 0);
            List<String> finalizeOperator = processMapper.selectOperator(contractNum, 1);
            List<String> reviewOperator = processMapper.selectOperator(contractNum, 2);
            List<String> finishSign = processMapper.selectOperatorWithState(contractNum,3,1);
            List<String> unfinishedSign = processMapper.selectOperatorWithState(contractNum,3,0);
           //List<String> signOperator = processMapper.selectOperator(contractNum, 3);
            //assignOperator.forEach(assign -> assigner.add(new Message(assign, "分配", "已完成")));
            countersignOperator.forEach(countersign -> counterSigners.add(new Message(countersign, "会签", "已完成")));
            finalizeOperator.forEach(finalize -> finalizer.add(new Message(finalize, "定稿", "已完成")));
            reviewOperator.forEach(review -> reviewers.add(new Message(review, "审核", "已完成")));
            finishSign.forEach(sign -> signers.add(new Message(sign, "签订", "已完成")));
            unfinishedSign.forEach(sign -> signers.add(new Message(sign, "签订", "未完成")));
        } else if (stateMapper.getContractStatus(contractNum) == 6) {
            List<String> countersignOperator = processMapper.selectOperator(contractNum, 0);
            List<String> finalizeOperator = processMapper.selectOperator(contractNum, 1);
            List<String> reviewOperator = processMapper.selectOperator(contractNum, 2);
            List<String> signOperator = processMapper.selectOperator(contractNum, 3);
            //assignOperator.forEach(assign -> assigner.add(new Message(assign, "分配", "已完成")));
            countersignOperator.forEach(countersign -> counterSigners.add(new Message(countersign, "会签", "已完成")));
            finalizeOperator.forEach(finalize -> finalizer.add(new Message(finalize, "定稿", "已完成")));
            reviewOperator.forEach(review -> reviewers.add(new Message(review, "审核", "已完成")));
            signOperator.forEach(sign -> signers.add(new Message(sign, "签订", "已完成")));
        }
        DetailContractMessage detailContractMessage = new DetailContractMessage(contract, lists);
        return ResponseFactory.success(detailContractMessage);
    }

    public ResponseEntity<PreviousProcessMessage> getPreviousProcessMessage(int contractNum, int type) {
        List<PreviousMessage> drafter = new ArrayList<>();
        List<PreviousMessage> counterSigners = new ArrayList<>();
        List<PreviousMessage> finalizerNeeds = new ArrayList<>();
        List<PreviousMessage> reviewers = new ArrayList<>();
        Contract contract = selectContractByNum(contractNum);
        PreviousProcessMessage previousProcessMessage = null;
        switch (type) {
            case 0:
                drafter.add(new PreviousMessage(contract.getUserName(), "起草", contract.getContent()));
                previousProcessMessage = new PreviousProcessMessage(contract, drafter);
                break;
            case 1:
                List<ContractProcess> countersign = processMapper.selectFinishedProcesses(contractNum, 0);
                countersign.forEach(process -> counterSigners.add(new PreviousMessage(process.getUserName(), "会签", process.getContent())));
                previousProcessMessage = new PreviousProcessMessage(contract, counterSigners);
                break;
            case 2:
                List<ContractProcess> finalizeNeed = processMapper.selectFinishedProcesses(contractNum, 0);
                finalizeNeed.forEach(process -> finalizerNeeds.add(new PreviousMessage(process.getUserName(), "会签", process.getContent())));
                previousProcessMessage = new PreviousProcessMessage(contract, finalizerNeeds);
                break;
            case 3:
                List<ContractProcess> review = processMapper.selectFinishedProcesses(contractNum, 2);
                review.forEach(process -> reviewers.add(new PreviousMessage(process.getUserName(), "审核", process.getContent())));
                previousProcessMessage = new PreviousProcessMessage(contract, reviewers);
                break;
        }


        return ResponseFactory.success(previousProcessMessage);
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
