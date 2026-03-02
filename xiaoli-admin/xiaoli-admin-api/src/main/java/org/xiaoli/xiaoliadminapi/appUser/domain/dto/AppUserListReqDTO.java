package org.xiaoli.xiaoliadminapi.appUser.domain.dto;


import lombok.Data;
import org.xiaoli.xiaolicommondomain.domain.dto.BasePageReqDTO;

import java.io.Serializable;

/**
 * 查询C端用户DTO
 */
@Data
public class AppUserListReqDTO extends BasePageReqDTO  implements Serializable {


    /**
     * 用户ID
     */
    private Long userId;


    /**
     * 手机号
     */
    private String phoneNumber;


    /**
     * 昵称
     */
    private String nickName;


    /**
     * 微信openId
     */
    private String openId;

}
