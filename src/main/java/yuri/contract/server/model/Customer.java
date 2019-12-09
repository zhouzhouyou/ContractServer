package yuri.contract.server.model;

import lombok.Data;

/**
 * 客户表
 */
@Data
public class Customer {
    /**
     * 客户编号
     */
    private Integer num;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 电话
     */
    private String tel;

    /**
     * 传真
     */
    private String fax;

    /**
     * 邮编
     */
    private String code;

    /**
     * 银行名称
     */
    private String bank;

    /**
     * 银行账户
     */
    private String account;

    /**
     * 备注
     */
    private String other;
}
