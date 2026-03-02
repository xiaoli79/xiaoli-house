package org.xiaoli.xiaoliadminapi.config.domain.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 新增字典数据DTO
 */
@Data
public class DictionaryDataAddReqDTO {

    /**
     * 字典类型业务主键
     */

    @NotBlank(message = "字典类型业务主键不能为空")
    //不仅可以判断是否为空，也可以判断字符串是否为空
    private String typeKey;

    /**
     * 字典数据业务主键
     */
    @NotBlank(message = "字典数据业务主键不能为空")
    private String dataKey;

    /**
     * 字典数据名称
     */
    @NotBlank(message = "字典数据名称不能为空")
    private String value;

    /**
     * 字典数据排序
     */
    private Integer sort;

    /**
     * 字典数据的备注
     */
    private String remark;

}
