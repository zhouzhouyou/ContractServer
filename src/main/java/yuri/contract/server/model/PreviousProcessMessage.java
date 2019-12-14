package yuri.contract.server.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("进行合同操作所要看的内容")
public class PreviousProcessMessage {
    /**
     * 合同
     */
    @ApiModelProperty("合同")
    private Contract contract;
    /**
     * 信息列表
     */
    @ApiModelProperty("信息列表")
    private List<PreviousMessage> messageList;

    public PreviousProcessMessage(Contract contract,List<PreviousMessage> messageList) {
        this.contract = contract;
        this.messageList = messageList;
    }



    @Data
    @ApiModel("各种操作的人员及操作内容")
    public static class PreviousMessage{
        /**
         * 操作人
         */
        @ApiModelProperty("操作人的姓名")
        private String operator;
        /**
         * 操作类型
         */
        @ApiModelProperty("操作类型")
        private String type;
        /**
         * 操作内容
         */
        @ApiModelProperty("操作内容")
        private String content;

        public PreviousMessage(String operator, String type, String content) {
            this.operator = operator;
            this.type = type;
            this.content = content;
        }
    }
}
