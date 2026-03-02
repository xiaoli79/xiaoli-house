package org.xiaoli.xiaoliportalservice.user.service;


import org.xiaoli.xiaoliadminapi.appUser.domain.dto.UserEditReqDTO;
import org.xiaoli.xiaolicommonsecurity.domain.dto.TokenDTO;
import org.xiaoli.xiaoliportalservice.user.entity.dto.LoginDTO;
import org.xiaoli.xiaoliportalservice.user.entity.dto.UserDTO;
import org.xiaoli.xiaoliportalservice.user.entity.vo.UserVo;

/**
 * 门户服务用户接口
 */
public interface IUserService {




    TokenDTO login(LoginDTO loginDTO);




    /**
     * 发送验证码
     * @param phone
     * @return
     */
    Boolean sendCode(String phone);


    /**
     * 编辑C端用户
     * @param userEditReqDTO C段用户DTO
     * @return
     */
    void edit(UserEditReqDTO userEditReqDTO);


    /**
     * 获取C端登录用户信息
     * @return
     */
    UserDTO getLoginUser();

    /**
     * 退出登录
     */
    void logout();
}
