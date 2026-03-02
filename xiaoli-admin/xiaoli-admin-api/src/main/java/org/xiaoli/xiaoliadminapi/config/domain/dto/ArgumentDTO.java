package org.xiaoli.xiaoliadminapi.config.domain.dto;


import lombok.Data;


/**
 * 远程调用参数对象DTO
 */
@Data
public class ArgumentDTO {


    /**
     * 自增主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;


    /**
     * 业务主键
     */
    private String configKey;

    /**
     * 值
     */
    private String value;

    /**
     * 备注
     */
    private String remark;













}
