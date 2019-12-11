package yuri.contract.server.model;

import lombok.Data;

import java.util.List;

@Data
public class DetailContractMessage {
    /**
     * 合同
     */
    private Contract contract;

    private List<List<Message>> messageLists;

    public DetailContractMessage(Contract contract,List<List<Message>> messageLists) {
        this.contract = contract;
        this.messageLists = messageLists;
    }

    public static class Message{
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

        public Message(String operator, String type, String state) {
            this.operator = operator;
            this.type = type;
            this.state = state;
        }
    }
}
