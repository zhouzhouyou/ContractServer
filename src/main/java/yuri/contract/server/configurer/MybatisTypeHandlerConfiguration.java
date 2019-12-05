package yuri.contract.server.configurer;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yuri.contract.server.handler.EnumTypeHandler;
import yuri.contract.server.model.util.EnumValue;
import yuri.contract.server.util.SpringClassScanner;

import java.util.List;
import java.util.function.Predicate;

import static yuri.contract.server.util.BaseInfo.MODEL_PACKAGE;

@Configuration
@ConditionalOnClass({SqlSessionFactory.class})
public class MybatisTypeHandlerConfiguration {
    private TypeHandlerRegistry typeHandlerRegistry;

    private final SpringClassScanner scanner;

    public MybatisTypeHandlerConfiguration(SqlSessionFactory sqlSessionFactory, SpringClassScanner springClassScanner) {
        this.typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();
        this.scanner = springClassScanner;
    }
    /**
     * 扫描所有的 {@link EnumValue} 实现类
     * 注册到 Spring 中
     *
     * @return 类集合
     */
    @Bean
    public List<Class<?>> enumValues() {
        // 过滤自定义枚举类
        Predicate<Class<?>> filter = clazz -> clazz.isEnum() && EnumValue.class.isAssignableFrom(clazz);
        return scanner.scanClass(MODEL_PACKAGE, filter);
    }



    /**
     * 注册 Mybatis 类型转换器
     */
    @Autowired
    public void registerTypeHandlers() {
        enumValues().forEach(this::registerEnumTypeHandler);
    }

    /**
     * 注册 枚举 类型的类型转换器
     *
     * @param javaTypeClass Java 类型
     */
    private void registerEnumTypeHandler(Class<?> javaTypeClass) {
        register(javaTypeClass, EnumTypeHandler.class);
    }

    /**
     * 注册类型转换器
     *
     * @param javaTypeClass    Java 类型
     * @param typeHandlerClass 类型转换器类型
     */
    private void register(Class<?> javaTypeClass, Class<?> typeHandlerClass) {
        this.typeHandlerRegistry.register(javaTypeClass, typeHandlerClass);
    }
}
