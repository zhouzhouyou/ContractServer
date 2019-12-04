package yuri.contract.server.model;

import lombok.Data;

import java.sql.Date;

/**
 * 合同表
 */
@Data
public class Contract {
    /**
     * 合同编号
     */
    private String num;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 客户
     */
    private String customer;

    /**
     * 开始时间
     */
    private Date begin;

    /**
     * 结束时间
     */
    private Date end;

    /**
     * 合同内容
     */
    private String content;

    /**
     * 起草人
     */
    private String userName;
}
