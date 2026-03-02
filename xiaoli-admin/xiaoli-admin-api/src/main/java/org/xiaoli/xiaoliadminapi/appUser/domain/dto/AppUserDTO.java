package org.xiaoli.xiaoliadminapi.appUser.domain.dto;


import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.xiaoli.xiaoliadminapi.appUser.domain.vo.AppUserVO;

import java.io.Serializable;

/**
 * C端用户的DTO
 */

@Data
public class AppUserDTO  implements Serializable {


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


    /**
     * DTO对象转换VO对象
     * @return
     */
    public AppUserVO convertToVO(){
        AppUserVO appUserVO = new AppUserVO();
        BeanUtils.copyProperties(this,appUserVO);
        return appUserVO;
    }


















}
