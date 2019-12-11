package yuri.contract.server.model;

import lombok.Data;

@Data
public class DetailContractMessage {
    /**
     * 合同
     */
    private Contract contract;

    /**
     * 操作人员
     */
    private String operator;

    /**
     * 操作类型
     */
    private String type;

    /**
     * 完成状态
     */
    private String state;

    public DetailContractMessage(Contract contract, String operator, String type, String state) {
        this.contract = contract;
        this.operator = operator;
        this.type = type;
        this.state = state;
    }
}
