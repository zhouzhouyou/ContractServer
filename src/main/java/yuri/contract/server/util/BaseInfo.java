package yuri.contract.server.util;

@SuppressWarnings("unused")
public class BaseInfo {
    /**
     * 项目基础包名称 ( 修改为 project 包的上级 )
     */
    public static final String BASE_PACKAGE = "yuri.contract.server";

    /**
     * 配置类所在包
     */
    public static final String CONFIGURATION_PACKAGE = BASE_PACKAGE + ".configurer";

    /**
     * 实体类所在包
     */
    public static final String MODEL_PACKAGE = BASE_PACKAGE + ".model";

    /**
     * Mapper 所在包
     */
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".mapper";

    /**
     * Service 所在包
     */
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";

    /**
     * Controller 所在包
     */
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".controller";
}
