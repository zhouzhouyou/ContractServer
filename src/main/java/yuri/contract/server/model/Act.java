package yuri.contract.server.model;

import lombok.Data;

/**
 * 权限表，记录角色和用户的多对多对应关系
 * 主键为{@link #userName}和{@link #roleName}。
 */
@Data
public class Act {
    /**
     * 用户名称
     */
    private String userName;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 描述
     */
    private String description;
}
