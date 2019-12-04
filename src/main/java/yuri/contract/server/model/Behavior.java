package yuri.contract.server.model;

import lombok.Data;

/**
 * 权限表，记录不同角色的不同功能的多对多的关系
 */
@Data
public class Behavior {
    /**
     * 角色名
     */
    private String roleName;

    /**
     * 功能编号
     */
    private String num;
}
