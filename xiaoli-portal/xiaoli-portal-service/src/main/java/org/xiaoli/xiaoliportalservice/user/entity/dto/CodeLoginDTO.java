package org.xiaoli.xiaoliportalservice.user.entity.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 验证码登录
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CodeLoginDTO extends LoginDTO{

    @NotBlank(message = "手机号不能为空")
    private String phone;


    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
}
