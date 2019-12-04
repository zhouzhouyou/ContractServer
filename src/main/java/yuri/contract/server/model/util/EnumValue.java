package yuri.contract.server.model.util;

import java.util.Objects;

/**
 * 为了便于枚举类型的维护
 */
public interface EnumValue {
    /**
     * 反序列化
     *
     * @param enumType 实际枚举类型
     * @param value    当前值
     * @param <T>      实现了该接口的枚举类型
     * @return 枚举常量
     */
    static <T extends Enum<T> & EnumValue> T valueOf(Class<T> enumType, Object value) {
        if (enumType == null || value == null) return null;
        T[] enumConstants = enumType.getEnumConstants();
        for (T enumConstant : enumConstants) {
            Object enumValue = enumConstant.toValue();
            if (Objects.equals(enumValue, value)
                    || Objects.equals(enumValue.toString(), value.toString()))
                return enumConstant;
        }
        return null;
    }

    /**
     * 序列化
     *
     * @return 不可以返回null
     */
    Object toValue();
}
