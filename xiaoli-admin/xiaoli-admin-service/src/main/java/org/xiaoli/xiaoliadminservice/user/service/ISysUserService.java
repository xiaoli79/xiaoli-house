package org.xiaoli.xiaoliadminservice.user.service;

import org.xiaoli.xiaoliadminservice.user.domain.dto.PasswordLoginDTO;
import org.xiaoli.xiaoliadminservice.user.domain.dto.SysUserDTO;
import org.xiaoli.xiaoliadminservice.user.domain.dto.SysUserListReqDTO;
import org.xiaoli.xiaoliadminservice.user.domain.dto.SysUserLoginDTO;
import org.xiaoli.xiaolicommonsecurity.domain.dto.TokenDTO;

import java.util.List;


/**
 * B端用户服务接口
 */
public interface ISysUserService {



    /**
     * B端用户登录
     * @param passwordLoginDTO
     * @return
     */
    TokenDTO login(PasswordLoginDTO passwordLoginDTO);

    /**
     * B端用户新增或编辑
     * @param sysUserDTO
     * @return
     */
    Long addOrEdit(SysUserDTO sysUserDTO);

    /**
     * B端用户的查询
     * @param sysUserListReqDTO
     * @return
     */
    List<SysUserDTO> getUserList(SysUserListReqDTO sysUserListReqDTO);


    /**
     * 获取B端用户登录信息
     * @return B端用户信息DTO
     */
    SysUserLoginDTO getLoginUser();
}
