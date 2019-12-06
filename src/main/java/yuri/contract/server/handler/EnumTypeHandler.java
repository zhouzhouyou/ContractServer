package yuri.contract.server.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import yuri.contract.server.model.util.EnumValue;
import yuri.contract.server.util.GenericsUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @param <T> 继承了EnumType的类
 * @author anyesu
 */
public class EnumTypeHandler<T extends Enum<T> & EnumValue> extends BaseTypeHandler<T> {
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    protected EnumTypeHandler() {
        type = GenericsUtils.getSuperClassGenericClass(getClass());
    }

    /**
     * 由 Mybatis 根据类型动态生成实例
     *
     * @param rawClass 原类型
     * @see org.apache.ibatis.type.TypeHandlerRegistry#getInstance(Class, Class)
     */
    public EnumTypeHandler(Class<T> rawClass) {
        this.type = rawClass;
    }

    private T valueOf(String s) {
        return s == null ? null : EnumValue.valueOf(type, s);
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException {
        Object value = t.toValue();
        if (jdbcType == null) preparedStatement.setObject(i, value);
        else preparedStatement.setObject(i, value, jdbcType.TYPE_CODE);
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return valueOf(resultSet.getString(s));
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return valueOf(resultSet.getString(i));
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return valueOf(callableStatement.getString(i));
    }
}
