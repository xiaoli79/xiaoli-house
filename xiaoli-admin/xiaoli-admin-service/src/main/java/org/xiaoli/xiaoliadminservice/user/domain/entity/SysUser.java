package org.xiaoli.xiaoliadminservice.user.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaoli.xiaolicommoncore.domain.entity.BaseDO;

/**
 * 系统用户表
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseDO {


    /**
     * 昵称
     */
    private String nickName;


    /**
     * 手机号
     */
    private String phoneNumber;


    /**
     * 密码
     */
    private String password;


    /**
     * 身份
     */
    private String identity;


    /**
     * 备注
     */
    private String remark;


    /**
     * 状态
     */
    private String status;

}
