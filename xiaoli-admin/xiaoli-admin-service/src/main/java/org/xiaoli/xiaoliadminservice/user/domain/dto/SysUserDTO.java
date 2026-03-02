package org.xiaoli.xiaoliadminservice.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.xiaoli.xiaoliadminservice.user.domain.vo.SysUserVO;

@Data
public class SysUserDTO {

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
    @NotBlank(message = "用户昵称不能为空")
    private String nickName;

    /**
     * 用户状态
     */
    @NotBlank(message = "用户的状态不能为空")
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用户密码
     */
    private String password;


    /**
     * 用户密码校验
     * @param password
     * @return
     */
    public boolean checkPassword(String password){
        if(StringUtils.isEmpty(password)){
            return false;
        }
        if(this.password.length() > 20){
            return false;
        }

        return this.password.matches("^[a-zA-Z0-9]+$");


    }

    /**
     * DTO转换成VO
     * @return
     */
    public SysUserVO convertToVO(){
        SysUserVO sysUserVO = new SysUserVO();
        sysUserVO.setUserId(this.userId);
        sysUserVO.setIdentity(this.identity);
        sysUserVO.setPhoneNumber(this.phoneNumber);
        sysUserVO.setNickName(this.nickName);
        sysUserVO.setStatus(this.status);
        sysUserVO.setRemark(this.remark);
        return sysUserVO;

    }

}
