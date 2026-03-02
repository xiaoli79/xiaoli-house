package org.xiaoli.xiaoliadminservice.user.domain.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * 用户查询接口的入参
 */
@Data
public class SysUserListReqDTO implements Serializable {


    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户手机号
     */
    private String phoneNumber;


    /**
     * 用户状态
     */
    private String status;

}
