package yuri.contract.server.controlller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yuri.contract.server.model.*;
import yuri.contract.server.model.util.OperationState;
import yuri.contract.server.model.util.OperationType;
import yuri.contract.server.service.ContractService;
import yuri.contract.server.util.annotation.NeedToken;
import yuri.contract.server.util.response.ResponseFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "合同相关操作接口")
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
        String operator = getOperator();
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.deleteContract(operator, contractNum.contractNum);
    }

    @ApiOperation("起草合同(就是添加合同)")
    @CrossOrigin
    @PutMapping(value = "/draft")
    @ResponseBody
    @NeedToken(function = NeedToken.DRAFT_CONTRACT)
    public ResponseEntity<String> draftContract(@RequestBody Contract contract, BindingResult bindingResult) {
        String operator = getOperator();
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.addContract(operator, contract);
    }

    @ApiOperation("按合同编号查询合同详细信息")
    @CrossOrigin
    @PostMapping(value = "/select")
    @ResponseBody
    @NeedToken(function = NeedToken.SELECT_PROCESS)
    public ResponseEntity<DetailContractMessage> selectContract(@RequestBody ContractNum contractNum, BindingResult bindingResult) {
        String operator = getOperator();
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.getDetailContractMessage(operator, contractNum.contractNum);
    }

    @ApiOperation("查询所有存在的合同信息")
    @CrossOrigin
    @PostMapping(value = "/selectAll")
    @ResponseBody
    @NeedToken(function = NeedToken.SELECT_CONTRACT)
    public ResponseEntity<List<ContractWithState>> selectAllContracts() {
        return contractService.selectAllContractsWithState();
    }

    @ApiOperation("模糊查询所有存在的合同信息")
    @CrossOrigin
    @PostMapping(value = "/fuzzyQuery")
    @ResponseBody
    @NeedToken(function = NeedToken.SELECT_CONTRACT)
    public ResponseEntity<List<ContractWithState>> fuzzyQueryAllContracts(@RequestBody FuzzyContent content, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.fuzzyQueryAllContract(content.content);
    }

    @ApiOperation("条件查询所有存在的合同信息")
    @CrossOrigin
    @PostMapping(value = "/filterQuery")
    @ResponseBody
    @NeedToken(function = NeedToken.SELECT_CONTRACT)
    public ResponseEntity<List<ContractWithState>> filterQueryAllContents(@RequestBody Filter filter, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.filterQueryAllContract(filter.statuses, filter.customerNum);
    }

    @ApiOperation("上传附件本体")
    @CrossOrigin
    @PostMapping(value = "/attachment/upload")
    @ResponseBody
    public ResponseEntity<String> uploadFile(@RequestParam("file_data") MultipartFile file, HttpServletRequest request) {
//        if (bindingResult.hasErrors())
//            return ResponseFactory.badRequest(null);
        return contractService.uploadFile(file);
    }

    @ApiOperation("下载附件")
    @CrossOrigin
    @PostMapping(value = "/attachment/download")
    @ResponseBody
    public ResponseEntity<String> downloadFile(@RequestBody ContractNum contractNum ,BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.downloadFile(contractNum.contractNum);

    }

    /*@ApiOperation("添加附件")
    @CrossOrigin
    @PutMapping(value = "/attachment/add")
    @ResponseBody
    @NeedToken(function = NeedToken.DRAFT_CONTRACT)
    public ResponseEntity<String> addContractAttachment(@RequestBody ContractAttachment attachment, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.addContractAttachment(getOperator(), attachment);
    }*/

    @ApiOperation("查询此用户是否有未完成的工作")
    @CrossOrigin
    @PostMapping(value = "/UnfinishedJobs/select")
    @ResponseBody
    public ResponseEntity<List<Boolean>> hasJobsToDo() {
        return contractService.hasJobsToDo(getOperator());
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
    public ResponseEntity<String> doAssignJob(@RequestBody Assign assign, BindingResult bindingResult) {
        String operator = getOperator();
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.doAssignJob(operator, assign.assignLists, assign.contractNum);
    }

    @ApiOperation("获取可会签的合同列表")
    @CrossOrigin
    @PostMapping(value = "/countersign/select")
    @ResponseBody
    @NeedToken(function = NeedToken.COUNTER_SIGN_CONTRACT)
    public ResponseEntity<List<Contract>> getAllUnCounterSign() {
        String operator = getOperator();
        return contractService.selectAllNeededContracts(operator, OperationType.COUNTER_SIGH.getValue());
    }

    @ApiOperation("模糊查询可会签的合同列表")
    @CrossOrigin
    @PostMapping(value = "/fuzzy/countersign/select")
    @ResponseBody
    @NeedToken(function = NeedToken.COUNTER_SIGN_CONTRACT)
    public ResponseEntity<List<Contract>> fuzzyAllUnCounterSign(@RequestBody FuzzyContent content, BindingResult bindingResult) {
        String operator = getOperator();
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.fuzzySelectAllNeededContracts(operator, content.content, OperationType.COUNTER_SIGH.getValue());
    }

    @ApiOperation("进行会签工作")
    @CrossOrigin
    @PutMapping(value = "/countersign/add")
    @ResponseBody
    @NeedToken(function = NeedToken.COUNTER_SIGN_CONTRACT)
    public ResponseEntity<String> doCountersignJob(@RequestBody ProcessJob processJob, BindingResult bindingResult) {
        String operator = getOperator();
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.doProcessJob(operator, processJob.contractNum,processJob.content, OperationType.COUNTER_SIGH.getValue(),processJob.state);
    }

    @ApiOperation("获取可定稿的合同列表")
    @CrossOrigin
    @PostMapping(value = "/finalize/select")
    @ResponseBody
    @NeedToken(function = NeedToken.FINALIZE_CONTRACT)
    public ResponseEntity<List<Contract>> getAllUnFinalizedContracts() {
        String operator = getOperator();
        return contractService.selectAllNeededContracts(operator, OperationType.FINALIZE.getValue());
    }

    @ApiOperation("模糊查询可定稿的合同列表")
    @CrossOrigin
    @PostMapping(value = "/fuzzy/finalize/select")
    @ResponseBody
    @NeedToken(function = NeedToken.FINALIZE_CONTRACT)
    public ResponseEntity<List<Contract>> fuzzyGetAllUnFinalizedContracts(@RequestBody FuzzyContent content, BindingResult bindingResult) {
        String operator = getOperator();
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.fuzzySelectAllNeededContracts(operator, content.content, OperationType.FINALIZE.getValue());
    }

    @ApiOperation("进行定稿工作")
    @CrossOrigin
    @PutMapping(value = "/finalize/add")
    @ResponseBody
    @NeedToken(function = NeedToken.FINALIZE_CONTRACT)
    public ResponseEntity<String> doFinalizeJob(@RequestBody ProcessJob processJob, BindingResult bindingResult) {
        String operator = getOperator();
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.doProcessJob(operator, processJob.contractNum,processJob.content, OperationType.FINALIZE.getValue(),processJob.state);
    }

    @ApiOperation("获取可审核的合同列表")
    @CrossOrigin
    @PostMapping(value = "/review/select")
    @ResponseBody
    @NeedToken(function = NeedToken.REVIEW_CONTRACT)
    public ResponseEntity<List<Contract>> getAllUnReviewedContracts() {
        String operator = getOperator();
        return contractService.selectAllNeededContracts(operator, OperationType.REVIEW.getValue());
    }

    @ApiOperation("模糊查询可审核的合同列表")
    @CrossOrigin
    @PostMapping(value = "/fuzzy/review/select")
    @ResponseBody
    @NeedToken(function = NeedToken.REVIEW_CONTRACT)
    public ResponseEntity<List<Contract>> fuzzyGetAllUnReviewedContracts(@RequestBody FuzzyContent content, BindingResult bindingResult) {
        String operator = getOperator();
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.fuzzySelectAllNeededContracts(operator, content.content, OperationType.REVIEW.getValue());
    }

    @ApiOperation("进行审核工作")
    @CrossOrigin
    @PutMapping(value = "/review/add")
    @ResponseBody
    @NeedToken(function = NeedToken.REVIEW_CONTRACT)
    public ResponseEntity<String> doReviewJob(@RequestBody ProcessJob processJob, BindingResult bindingResult) {
        String operator = getOperator();
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.doProcessJob(operator, processJob.contractNum,processJob.content, OperationType.REVIEW.getValue(),processJob.state);
    }

    @ApiOperation("获取可签订的合同列表")
    @CrossOrigin
    @PostMapping(value = "/sign/select")
    @ResponseBody
    @NeedToken(function = NeedToken.SIGN_CONTRACT)
    public ResponseEntity<List<Contract>> getAllUnSignedContracts() {
        String operator = getOperator();
        return contractService.selectAllNeededContracts(operator, OperationType.SIGN.getValue());
    }

    @ApiOperation("模糊查询可签订的合同列表")
    @CrossOrigin
    @PostMapping(value = "/fuzzy/sign/select")
    @ResponseBody
    @NeedToken(function = NeedToken.SIGN_CONTRACT)
    public ResponseEntity<List<Contract>> fuzzyGetAllUnSignedContracts(@RequestBody FuzzyContent content, BindingResult bindingResult) {
        String operator = getOperator();
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.fuzzySelectAllNeededContracts(operator, content.content, OperationType.SIGN.getValue());
    }

    @ApiOperation("进行签订工作")
    @CrossOrigin
    @PutMapping(value = "/sign/add")
    @ResponseBody
    @NeedToken(function = NeedToken.SIGN_CONTRACT)
    public ResponseEntity<String> doSignJob(@RequestBody ProcessJob processJob, BindingResult bindingResult) {
        String operator = getOperator();
        if (bindingResult.hasErrors())
            return ResponseFactory.badRequest(bindingResult.getFieldError().getDefaultMessage());
        return contractService.doProcessJob(operator, processJob.contractNum,processJob.content, OperationType.SIGN.getValue(),processJob.state);
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

    @ApiOperation("会签时获取合同初稿")
    @CrossOrigin
    @PostMapping(value = "/countersign_need_message/select")
    @ResponseBody
    @NeedToken(function = NeedToken.COUNTER_SIGN_CONTRACT)
    public ResponseEntity<PreviousProcessMessage> getCounterSignNeed(@RequestBody ContractNum contractNum,BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.getPreviousProcessMessage(contractNum.contractNum,OperationType.COUNTER_SIGH.getValue());
    }

    @ApiOperation("定稿时获取合同初稿和会签意见")
    @CrossOrigin
    @PostMapping(value = "/finalize_need_message/select")
    @ResponseBody
    @NeedToken(function = NeedToken.FINALIZE_CONTRACT)
    public ResponseEntity<PreviousProcessMessage> getFinalizeNeed(@RequestBody ContractNum contractNum,BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.getPreviousProcessMessage(contractNum.contractNum,OperationType.FINALIZE.getValue());
    }

    @ApiOperation("审核时获取合同终稿和会签意见")
    @CrossOrigin
    @PostMapping(value = "/review_need_message/select")
    @ResponseBody
    @NeedToken(function = NeedToken.REVIEW_CONTRACT)
    public ResponseEntity<PreviousProcessMessage> getReviewNeed(@RequestBody ContractNum contractNum,BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.getPreviousProcessMessage(contractNum.contractNum,OperationType.REVIEW.getValue());
    }

    @ApiOperation("签订时获取合同终稿和审核意见")
    @CrossOrigin
    @PostMapping(value = "/sign_need_message/select")
    @ResponseBody
    @NeedToken(function = NeedToken.SIGN_CONTRACT)
    public ResponseEntity<PreviousProcessMessage> getSignNeed(@RequestBody ContractNum contractNum,BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return ResponseFactory.badRequest(null);
        return contractService.getPreviousProcessMessage(contractNum.contractNum,OperationType.SIGN.getValue());
    }


    @Data
    @ApiModel("合同编号")
    private static class ContractNum {
        /**
         * 合同编号
         */
        @ApiModelProperty("合同编号")
        private int contractNum;
    }

    @Data
    @ApiModel("模糊查询关键字")
    private static class FuzzyContent {
        /**
         * 模糊查询关键字
         */
        @ApiModelProperty("模糊查询关键字")
        private String content;
    }

    @Data
    @ApiModel("分配任务所需信息")
    private static class Assign {
        /**
         * 分配任务列表
         */
        @ApiModelProperty("分配任务列表")
        private List<List<String>> assignLists;
        /**
         * 合同编号
         */
        @ApiModelProperty("合同编号")
        private int contractNum;
    }

    @Data
    @ApiModel("合同条件查询列表")
    private static class Filter {
        /**
         * 有无任务判断列表
         */
        @ApiModelProperty("状态列表")
        private boolean[] statuses;
        /**
         * 客户编号
         */
        @ApiModelProperty("客户编号")
        private Integer customerNum;
    }

    @Data
    @ApiModel("进行合同操作所需信息")
    private static class ProcessJob{
        /**
         * 合同号
         */
        @ApiModelProperty("合同编号")
        private Integer contractNum;
        /**
         * 操作内容
         */
        @ApiModelProperty("操作内容")
        private String content;
        /**
         * 操作结果
         */
        @ApiModelProperty("操作结果")
        private Integer state;
    }
}
