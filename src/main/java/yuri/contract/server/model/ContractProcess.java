package yuri.contract.server.model;

import lombok.Data;
import yuri.contract.server.model.util.OperationState;
import yuri.contract.server.model.util.OperationType;

import java.sql.Date;

/**
 * 合同操作流程信息
 * 当管理员分配完合同后，会向该表中插入合同分配的信息，用来记录该合同的会签人、定稿人（其实就是起草人），审核人和签订人
 */
@Data
public class ContractProcess {
    /**
     * 过程编号
     */
    private Integer processNum;
    /**
     * 合同编号
     */
    private Integer contractNum;

    /**
     * 操作类型（需要会签、定稿、审核还是签订？）
     */
    private OperationType type;

    /**
     * 操作状态（未完成、已完成、还是已否决？）
     */
    private OperationState state;

    /**
     * 操作人
     */
    private String userName;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 操作时间
     */
    private Date time;
}
