package yuri.contract.server.model;

import lombok.Data;

import java.sql.Date;

/**
 * 用户在系统中的操作，如涉及到数据的增加、删除或更改，建立日志信息，
 * 记录其姓名、用户名、操作日期时间，并保存到该表中
 */
@Data
public class ContractLog {
    /**
     * 自动增长的id
     */
    private Integer id;

    /**
     * 操作人员
     */
    private String userName;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 操作时间
     */
    private Date time;
}
