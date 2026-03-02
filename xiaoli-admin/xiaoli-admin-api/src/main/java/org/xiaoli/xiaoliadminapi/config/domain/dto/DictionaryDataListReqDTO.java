package org.xiaoli.xiaoliadminapi.config.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.xiaoli.xiaolicommondomain.domain.dto.BasePageReqDTO;

/**
 * 字典数据列表DTO
 */

@Data
public class DictionaryDataListReqDTO extends BasePageReqDTO {


    /**
     * 字典类型业务主键
     */
    @NotBlank(message = "字典类型业务主键不能为空")
    private String typeKey;


    /**
     * 字典类型数据
     */
    private String value;






}
