package org.xiaoli.xiaoliadminapi.appUser.domain.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * C端用户编辑DTO
 */
@Data
public class UserEditReqDTO {


    @NotNull(message =  "用户ID不能为空")
    private Long userId;


    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String avatar;

}
