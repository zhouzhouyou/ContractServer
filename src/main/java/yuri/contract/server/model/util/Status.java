package yuri.contract.server.model.util;

import lombok.AllArgsConstructor;

/**
 * 合同当前已经完成了什么步骤
 */
@AllArgsConstructor
public enum Status implements EnumValue {
    /**
     * 起草
     */
    DRAFT(0, "起草"),
    /**
     * 会签完成
     */
    COUNTER_SIGN_FINISHED(1, "会签完成"),
    /**
     * 定稿完成
     */
    FINALIZE_FINISHED(2, "定稿完成"),
    /**
     * 审核完成
     */
    REVIEW_FINISHED(3, "审核完成"),
    /**
     * 签订完成
     */
    SIGN_FINISHED(4, "签订完成");

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
