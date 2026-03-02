package org.xiaoli.xiaoliadminapi.config.domain.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 编辑参数
 */
@Data
public class ArgumentEditReqDTO {

    /**
     * 业务主键
     */
    @NotBlank(message = "业务主键不能为空")
    private String configKey;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数键值
     */
    private String value;

    /**
     * 参数备注
     */
    private String remark;
}
