package yuri.contract.server.model;

import lombok.Builder;
import lombok.Data;
import yuri.contract.server.model.util.Status;


@Data
public class ContractWithState {
    /**
     * 合同
     */
    private Contract contract;

    /**
     * 状态
     */
    private String status;

    public ContractWithState(Contract contract, String status) {
        this.contract = contract;
        this.status = status;
    }
}
