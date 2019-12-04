package yuri.contract.server.model;

import lombok.Data;

/**
 * 用户表，记录账户名和密码
 */
@Data
public class User {
    /**
     * 用户名称
     */
    private String name;

    /**
     * 密码
     */
    private String password;
}
