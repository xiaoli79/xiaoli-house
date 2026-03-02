package org.xiaoli.xiaoliadminservice.user.domain.vo;


import lombok.Data;

import java.io.Serializable;


/**
 * B端用户查询返回接口
 */

@Data
public class SysUserVO implements Serializable {


    /**
     * 用户ID
     */
    private Long userId;


    /**
     * 用户身份
     */
    private String identity;


    /**
     * 用户手机号
     */
    private String phoneNumber;


    /**
     * 用户昵称
     */
    private String nickName;


    /**
     * 用户状态
     */
    private String status;

    /**
     * 用户备注
     */
    private String remark;


}
