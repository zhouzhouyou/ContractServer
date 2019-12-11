package yuri.contract.server.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;

/**
 * 合同表
 */
@Data
@ApiModel
public class Contract {
    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    private int num;

    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称")
    private String name;

    /**
     * 客户编号
     */
    @ApiModelProperty(value = "客户编号")
    private Integer customerNum;

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
