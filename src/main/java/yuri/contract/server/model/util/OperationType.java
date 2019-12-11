package yuri.contract.server.model.util;

import lombok.AllArgsConstructor;

/**
 * 合同的不同阶段
 */
@AllArgsConstructor
public enum OperationType implements EnumValue {
    /**
     * 当前还需要分配
     */
    ASSIGN(-1,"分配"),
    /**
     * 当前还需要会签
     */
    COUNTER_SIGH(0, "会签"),
    /**
     * 所有会签人员会签结束，需要起草人定稿
     */
    FINALIZE(1, "定稿"),
    /**
     * 起草人定稿结束，需要审核人员审核
     */
    REVIEW(2, "审核"),
    /**
     * 审核通过，需要签订
     */
    SIGN(3, "签订");

    /**
     * 实际上存储的值
     */
    private final Integer value;

    /**
     * 描述
     */
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
