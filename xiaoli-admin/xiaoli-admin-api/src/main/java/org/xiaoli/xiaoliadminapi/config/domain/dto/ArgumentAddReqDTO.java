package org.xiaoli.xiaoliadminapi.config.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 新增参数请求DTO
 */
@Data
public class ArgumentAddReqDTO {


    /**
     * 参数业务主键
     */
    @NotBlank(message = "参数业务主键不能为空")
    private String configKey;


    @NotBlank(message = "参数名称不能为空")
    private String name;


    @NotBlank(message = "参数的值不能为空")
    private String value;

    /**
     * 备注字段
     */
    private String remark;


}
