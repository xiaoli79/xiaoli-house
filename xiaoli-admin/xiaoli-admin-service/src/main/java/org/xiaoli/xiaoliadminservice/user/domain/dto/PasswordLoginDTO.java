package org.xiaoli.xiaoliadminservice.user.domain.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;


/**
 * 登录的入参
 */
@Data
public class PasswordLoginDTO implements Serializable {


    /**
     * 登录的手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;


    /**
     * 第一次加密后的密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

}
