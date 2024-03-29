package yuri.contract.server.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 权限表，记录角色和用户的多对多对应关系
 * 主键为{@link #userName}和{@link #roleId}。
 */
@Data
@ApiModel(description = "用户扮演什么角色")
public class Act {
    /**
     * 用户名称
     */
    private String userName;

    /**
     * 角色名称
     */
    private Integer roleId;

    /**
     * 描述
     */
    private String description;
}
