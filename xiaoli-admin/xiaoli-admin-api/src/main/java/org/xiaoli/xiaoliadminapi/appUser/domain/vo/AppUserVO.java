package org.xiaoli.xiaoliadminapi.appUser.domain.vo;

import lombok.Data;

import java.io.Serializable;


/**
 * C端用户VO
 */
@Data
public class AppUserVO implements Serializable {




    /**
     * C端用户ID
     */
    private Long userId;


    /**
     * 用户ID
     */
    private String nickName;


    /**
     * 用户手机号
     */
    private String phoneNumber;


    /**
     * 微信Id
     */
    private String openId;

    /**
     * 用户头像
     */
    private String avatar;

}
