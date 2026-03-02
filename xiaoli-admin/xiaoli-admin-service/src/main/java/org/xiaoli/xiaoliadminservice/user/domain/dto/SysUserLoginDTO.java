package org.xiaoli.xiaoliadminservice.user.domain.dto;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.xiaoli.xiaoliadminservice.user.domain.vo.SysUserLoginVO;
import org.xiaoli.xiaolicommonsecurity.domain.dto.LoginUserDTO;



/**
 * B端登录用户信息
 */
@Data
public class SysUserLoginDTO extends LoginUserDTO {

    /**
     * 昵称
     */
    private String nickName;


    /**
     * 身份
     */
    private String identity;

    /**
     * 状态
     */
    private String status;



//  实现对象的转换从DTO到VO
    public SysUserLoginVO convertToVO() {
        SysUserLoginVO vo = new SysUserLoginVO();
        BeanUtils.copyProperties(this, vo);
        return vo;
    }


}
