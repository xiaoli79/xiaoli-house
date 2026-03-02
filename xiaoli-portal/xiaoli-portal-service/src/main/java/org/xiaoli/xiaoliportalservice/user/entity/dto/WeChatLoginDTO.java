package org.xiaoli.xiaoliportalservice.user.entity.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信登录信息栏
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class WeChatLoginDTO extends LoginDTO {


    /**
     * 微信OpenId
     */
    @NotBlank(message = "微信openId不能为空")
    private String openId;





}
