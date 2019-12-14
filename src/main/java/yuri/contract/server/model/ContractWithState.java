package yuri.contract.server.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import yuri.contract.server.model.util.Status;


@Data
@ApiModel("合同以及其流程状态")
public class ContractWithState {
    /**
     * 合同
     */
    @ApiModelProperty("合同")
    private Contract contract;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private String status;

    public ContractWithState(Contract contract, String status) {
        this.contract = contract;
        this.status = status;
    }
}
