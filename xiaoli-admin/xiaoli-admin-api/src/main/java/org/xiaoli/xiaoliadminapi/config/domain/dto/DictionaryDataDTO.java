package org.xiaoli.xiaoliadminapi.config.domain.dto;


import lombok.Data;

/**
 * 字典数据DTO
 */

@Data
public class DictionaryDataDTO {

    /**
     * 字典数据ID
     */
    private Long id;

    /**
     * 字典类型业务主键
     */
    private String typeKey;

    /**
     * 字典数据业务主键
     */
    private String dataKey;


    /**
     * 字典数据名称
     */
    private String value;


    /**
     * 备注
     */
    private String reamark;


    /**
     * 排序值
     */
    private String sort;

    /**
     * 字典数据状态
     */
    private Integer status;

}
