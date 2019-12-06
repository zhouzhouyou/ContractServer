package yuri.contract.server.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户表，记录账户名和密码
 */
@Data
@ApiModel(description = "用户名和密码")
public class User {
    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名", name = "name", example = "zzy")
    private String name;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码(应当要加密的)", name = "password")
    private String password;
}
