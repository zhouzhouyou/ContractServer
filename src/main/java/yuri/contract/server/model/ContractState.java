package yuri.contract.server.model;

import lombok.Data;
import yuri.contract.server.model.util.Status;

import java.sql.Date;

/**
 * 合同操作状态信息
 * <p>
 * 保存合同的起草、会签完成、定稿完成、审批完成、签订完成等状态信息。
 * 当涉及的合同操作人员，完成了合同的某一流程操作后，则将完成状态的合同信息保 存到该表中。
 * 如合同会签需要三个人进行，那么必须这三个人均会签完毕才能将最终的合同 “会签完成”状态信息保存到该表中。
 * </p>
 */
@Data
public class ContractState {
    /**
     * 合同编号
     */
    private Integer num;

    /**
     * 合同状态（起草、会签完成、定稿完成、审核完成还是签订完成？）
     */
    private Status status;

    /**
     * 完成时间
     */
    private Date time;
}
