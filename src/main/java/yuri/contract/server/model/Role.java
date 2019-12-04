package yuri.contract.server.model;

import lombok.Data;

/**
 * 角色表，记录有角色名和角色描述
 */
@Data
public class Role {
    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;
}
