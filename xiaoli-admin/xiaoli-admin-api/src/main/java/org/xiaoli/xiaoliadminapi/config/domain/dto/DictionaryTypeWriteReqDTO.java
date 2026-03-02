package org.xiaoli.xiaoliadminapi.config.domain.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DictionaryTypeWriteReqDTO {

    /**
     * 字典类型键
     */
    @NotNull(message = "字典类型值不能为空")
    private String value;

    /**
     * 字典类型值
     */
    @NotNull(message = "字典类型键不能为空")
    private String typeKey;

    /**
     * 备注
     */
    private String remark;

}
