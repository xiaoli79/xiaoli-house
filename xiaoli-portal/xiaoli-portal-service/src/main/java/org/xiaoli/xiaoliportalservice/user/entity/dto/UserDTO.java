package org.xiaoli.xiaoliportalservice.user.entity.dto;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.xiaoli.xiaolicommonsecurity.domain.dto.LoginUserDTO;
import org.xiaoli.xiaoliportalservice.user.entity.vo.UserVo;


@Data
public class UserDTO extends LoginUserDTO {



//  头像
    private String avatar;


    /**
     * 对象转换
     * @return
     */
    public UserVo convertToVO(){
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(this,userVo);
        userVo.setNickName(this.getUserName());
        return userVo;
    }
}

