package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import yuri.contract.server.model.Contract;
import yuri.contract.server.model.ContractAttachment;
import yuri.contract.server.model.ContractProcess;
import yuri.contract.server.model.util.OperationType;
import yuri.contract.server.service.ContractService;
import yuri.contract.server.util.annotation.NeedToken;
import yuri.contract.server.util.response.ResponseFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "To Control contract operations")
@RestController
@RequestMapping("/api/contract")
public class ContractController extends BaseController {
    private ContractService contractService;

    @Autowired
    public ContractController(HttpServletRequest request, ContractService contractService) {
        super(request);
        this.contractService = contractService;
    }

    @ApiOperation("删除合同")
    @CrossOrigin
    @DeleteMapping(value = "/delete")
    @ResponseBody
    @NeedToken(function = NeedToken.DELETE_CONTRACT)
    public ResponseEntity<String> deleteContract(@RequestBody ContractNum contractNum, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.deleteContract(getOperator(),contractNum.contractNum);
    }

    @ApiOperation("起草合同(就是添加合同)")
    @CrossOrigin
    @PutMapping(value = "/draft")
    @ResponseBody
    @NeedToken(function = NeedToken.DRAFT_CONTRACT)
    public ResponseEntity<String> draftContract(@RequestBody Contract contract, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.addContract(getOperator(), contract);
    }

    @ApiOperation("按合同编号查询合同")
    @CrossOrigin
    @PostMapping(value = "/select")
    @ResponseBody
    @NeedToken(function = NeedToken.SELECT_CONTRACT)
    public ResponseEntity<Contract> selectContract(@RequestBody ContractNum contractNum, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.selectContractByNum(contractNum.contractNum);
    }

    @ApiOperation("查询所有存在的合同")
    @CrossOrigin
    @PostMapping(value = "/selectAll")
    @ResponseBody
    @NeedToken(function = NeedToken.SELECT_CONTRACT)
    public ResponseEntity<List<Contract>> selectAllContracts() {
        return contractService.selectAllContracts();
    }

    @ApiOperation("添加附件")
    @CrossOrigin
    @PutMapping(value = "/attachment/add")
    @ResponseBody
    @NeedToken(function = NeedToken.DRAFT_CONTRACT)
    public ResponseEntity<String> addContractAttachment(@RequestBody ContractAttachment attachment, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.addContractAttachment(getOperator(), attachment);
    }

    @ApiOperation("获取所有未分配的合同")
    @CrossOrigin
    @PostMapping(value = "/UnAssigned/select")
    @ResponseBody
    @NeedToken(function = NeedToken.ASSIGN)
    public ResponseEntity<List<Contract>> getAllUnAssignedContracts() {
        return contractService.selectAllUnAssignedContracts();
    }

    @ApiOperation("模糊查询所有未分配的合同")
    @CrossOrigin
    @PostMapping(value = "/fuzzy/UnAssigned/select")
    @ResponseBody
    @NeedToken(function = NeedToken.ASSIGN)
    public ResponseEntity<List<Contract>> fuzzyAllUnAssignedContracts(@RequestBody FuzzyContent content, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.fuzzySelectAllUnAssignedContracts(content.content);
    }

    @ApiOperation("分配合同")
    @CrossOrigin
    @PutMapping(value = "/assign/add")
    @ResponseBody
    @NeedToken(function = NeedToken.ASSIGN)
    public ResponseEntity<String> doAssignJob(@RequestBody ContractProcess process, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.doAssignJob(getOperator(), process);
    }

    @ApiOperation("获取可会签的合同列表")
    @CrossOrigin
    @PostMapping(value = "/countersign/select")
    @ResponseBody
    @NeedToken(function = NeedToken.COUNTER_SIGN_CONTRACT)
    public ResponseEntity<List<Contract>> getAllUnCounterSign() {
        return contractService.selectAllNeededContracts(getOperator(), OperationType.COUNTER_SIGH.getValue());
    }

    @ApiOperation("模糊查询可会签的合同列表")
    @CrossOrigin
    @PostMapping(value = "/fuzzy/countersign/select")
    @ResponseBody
    @NeedToken(function = NeedToken.COUNTER_SIGN_CONTRACT)
    public ResponseEntity<List<Contract>> fuzzyAllUnCounterSign(@RequestBody FuzzyContent content, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.fuzzySelectAllNeededContracts(getOperator(), content.content, OperationType.COUNTER_SIGH.getValue());
    }

    @ApiOperation("进行会签工作")
    @CrossOrigin
    @PutMapping(value = "/countersign/add")
    @ResponseBody
    @NeedToken(function = NeedToken.COUNTER_SIGN_CONTRACT)
    public ResponseEntity<String> doCountersignJob(@RequestBody ContractProcess process, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.doProcessJob(getOperator(), process, OperationType.COUNTER_SIGH.getValue());
    }

    @ApiOperation("获取可定稿的合同列表")
    @CrossOrigin
    @PostMapping(value = "/finalize/select")
    @ResponseBody
    @NeedToken(function = NeedToken.FINALIZE_CONTRACT)
    public ResponseEntity<List<Contract>> getAllUnFinalizedContracts() {
        return contractService.selectAllNeededContracts(getOperator(), OperationType.FINALIZE.getValue());
    }

    @ApiOperation("模糊查询可定稿的合同列表")
    @CrossOrigin
    @PostMapping(value = "/fuzzy/finalize/select")
    @ResponseBody
    @NeedToken(function = NeedToken.FINALIZE_CONTRACT)
    public ResponseEntity<List<Contract>> fuzzyGetAllUnFinalizedContracts(@RequestBody FuzzyContent content, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.fuzzySelectAllNeededContracts(getOperator(), content.content, OperationType.FINALIZE.getValue());
    }

    @ApiOperation("进行定稿工作")
    @CrossOrigin
    @PutMapping(value = "/finalize/add")
    @ResponseBody
    @NeedToken(function = NeedToken.FINALIZE_CONTRACT)
    public ResponseEntity<String> doFinalizeJob(@RequestBody ContractProcess process, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.doProcessJob(getOperator(), process, OperationType.FINALIZE.getValue());
    }

    @ApiOperation("获取可审核的合同列表")
    @CrossOrigin
    @PostMapping(value = "/review/select")
    @ResponseBody
    @NeedToken(function = NeedToken.REVIEW_CONTRACT)
    public ResponseEntity<List<Contract>> getAllUnReviewedContracts() {
        return contractService.selectAllNeededContracts(getOperator(), OperationType.REVIEW.getValue());
    }

    @ApiOperation("模糊查询可审核的合同列表")
    @CrossOrigin
    @PostMapping(value = "/fuzzy/review/select")
    @ResponseBody
    @NeedToken(function = NeedToken.REVIEW_CONTRACT)
    public ResponseEntity<List<Contract>> fuzzyGetAllUnReviewedContracts(@RequestBody FuzzyContent content, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.fuzzySelectAllNeededContracts(getOperator(), content.content, OperationType.REVIEW.getValue());
    }

    @ApiOperation("进行审核工作")
    @CrossOrigin
    @PutMapping(value = "/review/add")
    @ResponseBody
    @NeedToken(function = NeedToken.REVIEW_CONTRACT)
    public ResponseEntity<String> doReviewJob(@RequestBody ContractProcess process, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.doProcessJob(getOperator(), process, OperationType.REVIEW.getValue());
    }

    @ApiOperation("获取可签订的合同列表")
    @CrossOrigin
    @PostMapping(value = "/sign/select")
    @ResponseBody
    @NeedToken(function = NeedToken.SIGN_CONTRACT)
    public ResponseEntity<List<Contract>> getAllUnSignedContracts() {
        return contractService.selectAllNeededContracts(getOperator(), OperationType.SIGN.getValue());
    }

    @ApiOperation("模糊查询可签订的合同列表")
    @CrossOrigin
    @PostMapping(value = "/fuzzy/sign/select")
    @ResponseBody
    @NeedToken(function = NeedToken.FINALIZE_CONTRACT)
    public ResponseEntity<List<Contract>> fuzzyGetAllUnSignedContracts(@RequestBody FuzzyContent content, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.fuzzySelectAllNeededContracts(getOperator(), content.content, OperationType.SIGN.getValue());
    }

    @ApiOperation("进行签订工作")
    @CrossOrigin
    @PutMapping(value = "/sign/add")
    @ResponseBody
    @NeedToken(function = NeedToken.FINALIZE_CONTRACT)
    public ResponseEntity<String> doSignJob(@RequestBody ContractProcess process, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.doProcessJob(getOperator(), process, OperationType.SIGN.getValue());
    }

    @ApiOperation("获取合同状态")
    @CrossOrigin
    @PostMapping(value = "/contract_status/select")
    @ResponseBody
    @NeedToken(function = NeedToken.SELECT_CONTRACT)
    public ResponseEntity<String> getContractStatus(@RequestBody ContractNum contractNum, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.getContractStatus(contractNum.contractNum);
    }

    @Data
    private static class ContractNum {
        private String contractNum;
    }

    @Data
    private static class FuzzyContent {
        private String content;
    }
}
