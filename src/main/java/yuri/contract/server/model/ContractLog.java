package yuri.contract.server.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 用户在系统中的操作，如涉及到数据的增加、删除或更改，建立日志信息，
 * 记录其姓名、用户名、操作日期时间，并保存到该表中
 */
@ApiModel("日志")
@Data
public class ContractLog {
    /**
     * 自动增长的id
     */
    @ApiModelProperty("日志序号")
    private Integer id;

    /**
     * 操作人员
     */
    @ApiModelProperty("用户名")
    private String userName;

    /**
     * 操作内容
     */
    @ApiModelProperty("日志内容")
    private String content;

    /**
     * 操作时间
     */
    @ApiModelProperty("日志时间")
    private Timestamp time;
}
