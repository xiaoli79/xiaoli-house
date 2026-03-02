package org.xiaoli.xiaoliadminapi.config.domain.dto;


import lombok.Data;
import org.xiaoli.xiaolicommondomain.domain.dto.BasePageReqDTO;

/**
 * 字典类型列表DTO
 */

@Data
public class DictionaryTypeListReqDTO extends BasePageReqDTO {

    /**
     * 字典类型的值
     */
    private String value;


    /**
     * 字典类型键
     */
    private String typeKey;

}
