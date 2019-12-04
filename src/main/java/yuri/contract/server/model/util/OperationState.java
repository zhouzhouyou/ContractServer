package yuri.contract.server.model.util;

import lombok.AllArgsConstructor;

/**
 * 合同某一阶段的完成情况
 */
@AllArgsConstructor
public enum OperationState implements EnumValue {
    /**
     * 未完成
     */
    UNFINISHED(0, "未完成"),
    /**
     * 已完成
     */
    FINISHED(1, "已完成"),
    /**
     * 已否决
     */
    DENIED(2, "已否决");

    private final Integer value;

    private final String desc;

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public Object toValue() {
        return value;
    }
}
