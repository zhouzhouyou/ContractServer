package yuri.contract.server.model;

import lombok.Data;

/**
 * 记录功能的编号，名字和描述
 * //TODO: 也许需要把功能名换成枚举？这个表其实是固定的内容~
 */
@Data
public class Function {
    /**
     * 功能编号
     */
    private String num;

    /**
     * 功能名
     */
    private String name;

    /**
     * 功能描述
     */
    private String description;
}
