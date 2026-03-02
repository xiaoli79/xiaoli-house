package org.xiaoli.xiaoliadminapi.config.domain.vo;


import lombok.Data;


/**
 * 字典数据列表DTO
 */
@Data
public class DictionaryDataVo {

    /**
     * 字典数据主键
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
     * 字典数据值
     */
    private String value;


    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sort;


    private Integer status;





















}
