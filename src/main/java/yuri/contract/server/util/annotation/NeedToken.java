package yuri.contract.server.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将这个注解使用在Controller中的方法上，例如
 * {@code @NeedToken(function = NeedToken.GRANT)}，
 * 这意味这个方法需要先检测http头中token对应的用户是否拥有GRANT权限
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedToken {
    /**
     * 是否需要权限
     * @return 是否需要权限
     */
    boolean required() default true;

    /**
     * 需要的是什么权限
     * @return 权限编号
     */
    String function();

    /**
     * 起草合同
     */
    String DRAFT_CONTRACT = "0";

    /**
     * 定稿合同
     */
    String FINALIZE_CONTRACT = "1";

    /**
     * 查询合同
     */
    String SELECT_CONTRACT = "2";

    /**
     * 删除合同
     */
    String DELETE_CONTRACT = "3";

    /**
     * 会签合同
     */
    String COUNTER_SIGN_CONTRACT = "4";

    /**
     * 审批合同
     */
    String REVIEW_CONTRACT = "5";

    /**
     * 签订合同
     */
    String SIGN_CONTRACT = "6";

    /**
     * 分配会签
     */
    String ASSIGN_COUNTER_SIGN = "7";

    /**
     * 分配审批
     */
    String ASSIGN_REVIEW = "8";

    /**
     * 分配签订
     */
    String ASSIGN_SIGN = "9";

    /**
     * 流程查询
     */
    String SELECT_PROCESS = "10";

    /**
     * 新增用户
     */
    String INSERT_USER = "11";

    /**
     * 编辑用户
     */
    String UPDATE_USER = "12";

    /**
     * 查询用户
     */
    String SELECT_USER = "13";

    /**
     * 删除用户
     */
    String DELETE_USER = "14";

    /**
     * 新增角色
     */
    String INSERT_ROLE = "15";

    /**
     * 编辑角色
     */
    String UPDATE_ROLE = "16";

    /**
     * 查询角色
     */
    String SELECT_ROLE = "17";

    /**
     * 删除角色
     */
    String DELETE_ROLE = "18";

    /**
     * 配置权限
     */
    String GRANT = "22";

    /**
     * 新增客户
     */
    String INSERT_CUSTOMER = "23";

    /**
     * 编辑客户
     */
    String UPDATE_CUSTOMER = "24";

    /**
     * 查询客户
     */
    String SELECT_CUSTOMER = "25";

    /**
     * 删除用户
     */
    String DELETE_CUSTOMER = "26";
}
