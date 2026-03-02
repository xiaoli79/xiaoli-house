package org.xiaoli.xiaoliadminapi.map.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import org.xiaoli.xiaolicommondomain.domain.dto.BasePageReqDTO;

/**
 * 查询请求的DTO
 */
@Data
public class PlaceSearchReqDTO extends BasePageReqDTO {


    /**
     *请求的关键字
     */
    @NotNull(message = "请求的关键字不能为空")
    private String keyword;


    /**
     *请求的区域ID
     */
    @NotNull(message = "请求的区域ID不能为空")
    private Long id;
}
