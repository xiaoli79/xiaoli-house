package org.xiaoli.xiaoliadminapi.config.domain.dto;


import lombok.Data;
import org.xiaoli.xiaolicommondomain.domain.dto.BasePageReqDTO;

/**
 * 查看参数的DTO
 */
@Data
public class ArgumentListReqDTO extends BasePageReqDTO {

    /**
     * 参数名称
     */
    private String name;


    /**
     * 参数业务主键
    */
    private String configKey;









}
