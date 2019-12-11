package yuri.contract.server.model;

import lombok.Data;

import java.sql.Date;

/**
 * 起草合同的时候，如果上传了合同附件，则将附件信息保存的该表中。
 */
@Data
public class ContractAttachment {
    /**
     * 合同编号
     */
    private int contractNum;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 上传路径
     */
    private String path;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 上传时间
     */
    private Date uploadTime;
}
