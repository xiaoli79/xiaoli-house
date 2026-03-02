package org.xiaoli.xiaoliportalservice.user.entity.vo;

import lombok.Data;
import org.xiaoli.xiaolicommonsecurity.domain.dto.LoginUserDTO;


@Data
public class UserVo extends LoginUserDTO {



    private String nickName;


    private String avatar;
}