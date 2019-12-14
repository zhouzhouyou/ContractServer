package yuri.contract.server.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("合同的流程详细相关信息")
public class DetailContractMessage {
    /**
     * 合同
     */
    @ApiModelProperty("合同")
    private Contract contract;
    /**
     * 信息列表
     */
    @ApiModelProperty("信息列表")
    private List<List<Message>> messageLists;

    public DetailContractMessage(Contract contract,List<List<Message>> messageLists) {
        this.contract = contract;
        this.messageLists = messageLists;
    }

    @Data
    @ApiModel("各种操作的人员及完成情况")
    public static class Message{
        /**
         * 操作人员
         */
        @ApiModelProperty("操作人员")
        private String operator;

        /**
         * 操作类型
         */
        @ApiModelProperty("操作类型")
        private String type;

        /**
         * 完成状态
         */
        @ApiModelProperty("完成状况")
        private String state;

        public Message(String operator, String type, String state) {
            this.operator = operator;
            this.type = type;
            this.state = state;
        }
    }
}
