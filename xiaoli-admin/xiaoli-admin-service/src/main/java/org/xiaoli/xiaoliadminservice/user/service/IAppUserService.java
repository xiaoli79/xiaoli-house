package org.xiaoli.xiaoliadminservice.user.service;

import org.xiaoli.xiaoliadminapi.appUser.domain.dto.AppUserDTO;
import org.xiaoli.xiaoliadminapi.appUser.domain.dto.AppUserListReqDTO;
import org.xiaoli.xiaoliadminapi.appUser.domain.dto.UserEditReqDTO;
import org.xiaoli.xiaolicommoncore.domain.dto.BasePageDTO;

import java.util.List;


public interface IAppUserService {



    /**
     * 根据微信注册用户
     * @param openId 用户微信ID
     * @return C端用户VO
     */
     AppUserDTO registerByOpenId (String openId);



    /**
     * 根据openid来查询用户信息
     * @param openId 用户微信ID
     * @return C端用户VO
     */
    AppUserDTO findByOpenId(String openId);



    /**
     * 根据手机号来查询用户信息
     * @param phoneNumber
     * @return
     */
    AppUserDTO findByPhone(String phoneNumber);

    /**
     * 根据手机号来注册用户信息
     * @param phoneNumber
     * @return
     */
    AppUserDTO registerByPhone(String phoneNumber);


    /**
     * 编辑C端用户
     * @param userEditReqDTO C段用户DTO
     * @return
     */
    void edit(UserEditReqDTO userEditReqDTO);



    /**
     * 查询C端用户
     * @param appUserListReqDTO 查询C端用户DTO
     * @return C端用户分页结果
     */
    BasePageDTO<AppUserDTO> getUserList(AppUserListReqDTO appUserListReqDTO);

    AppUserDTO findById(Long userId);

    List<AppUserDTO> getUserList(List<Long> userIds);
}
