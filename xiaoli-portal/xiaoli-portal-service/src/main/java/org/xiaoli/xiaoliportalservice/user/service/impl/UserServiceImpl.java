package org.xiaoli.xiaoliportalservice.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoli.xiaoliadminapi.appUser.domain.dto.UserEditReqDTO;
import org.xiaoli.xiaoliadminapi.appUser.domain.vo.AppUserVO;
import org.xiaoli.xiaoliadminapi.appUser.feign.AppUserFeignClient;
import org.xiaoli.xiaolicommoncore.utils.VerifyUtil;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;
import org.xiaoli.xiaolicommonmessage.service.AliPnsService;
import org.xiaoli.xiaolicommonsecurity.domain.dto.LoginUserDTO;
import org.xiaoli.xiaolicommonsecurity.domain.dto.TokenDTO;
import org.xiaoli.xiaolicommonsecurity.service.TokenService;
import org.xiaoli.xiaolicommonsecurity.utils.JwtUtil;
import org.xiaoli.xiaolicommonsecurity.utils.SecurityUtil;
import org.xiaoli.xiaoliportalservice.user.entity.dto.CodeLoginDTO;
import org.xiaoli.xiaoliportalservice.user.entity.dto.LoginDTO;
import org.xiaoli.xiaoliportalservice.user.entity.dto.UserDTO;
import org.xiaoli.xiaoliportalservice.user.entity.dto.WeChatLoginDTO;
import org.xiaoli.xiaoliportalservice.user.service.IUserService;


@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private AppUserFeignClient appUserFeignClient;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AliPnsService aliPnsService;


    /**
     * 用户登录DTO
     * @param loginDTO
     * @return
     */
    @Override
    public TokenDTO login(LoginDTO loginDTO) {
//       设置用户生命周期
        LoginUserDTO loginUserDTO = new LoginUserDTO();
//        针对入参进行了逻辑分发

        if(loginDTO instanceof WeChatLoginDTO weChatLoginDTO){
//          处理微信登录逻辑
            loginByWeChat(weChatLoginDTO,loginUserDTO);
        }else if(loginDTO instanceof CodeLoginDTO  codeLoginDTO){
            loginByCode(codeLoginDTO,loginUserDTO);
        }

//      4.设置缓存
        loginUserDTO.setUserFrom("app");
//      创建token
        return tokenService.createToken(loginUserDTO);
    }


    /**
     * 处理用户登录逻辑
     * @param codeLoginDTO
     * @param loginUserDTO
     */
    private void loginByCode(CodeLoginDTO codeLoginDTO, LoginUserDTO loginUserDTO) {
        if(!VerifyUtil.checkPhone(codeLoginDTO.getPhone())){
            throw new ServiceException("手机号格式错误",ResultCode.INVALID_PARA.getCode());
        }

//      先校验验证码
        if(!aliPnsService.checkPnsVerifyCode(codeLoginDTO.getPhone(),codeLoginDTO.getVerifyCode())){
            throw new ServiceException("验证码错误");
        }

//      验证码通过后再查询/注册用户
        AppUserVO appUserVO;

        R<AppUserVO> result = appUserFeignClient.findByPhone(codeLoginDTO.getPhone());

        if(result == null || result.getCode() != ResultCode.SUCCESS.getCode() || result.getData() == null){
            appUserVO = register(codeLoginDTO);
        }else{
            appUserVO = result.getData();
        }

        loginUserDTO.setUserId(appUserVO.getUserId());
        loginUserDTO.setUserName(appUserVO.getNickName());
    }


    /**
     * 发送短信的验证码
     * @param phone 手机号
     * @return 验证码
     */
    @Override
    public Boolean sendCode(String phone) {
        if(!VerifyUtil.checkPhone(phone)){
            throw new ServiceException("手机号格式错误",ResultCode.INVALID_PARA.getCode());
        }
        return aliPnsService.sendPnsVerifyCode(phone);
    }


    /**
     * 编辑C端用户
     * @param userEditReqDTO C段用户DTO
     * @return
     */
    @Override
    public void edit(UserEditReqDTO userEditReqDTO) {
        R<Void> result = appUserFeignClient.edit(userEditReqDTO);
        if(result == null || result.getCode() != ResultCode.SUCCESS.getCode()){
            throw new ServiceException("修改用户失败",ResultCode.INVALID_PARA.getCode());
        }

    }


    /**
     * 获取C端登录用户信息
     * @return
     */
    @Override
    public UserDTO getLoginUser() {
//      获取当前登录用户
        LoginUserDTO loginUserDTO = tokenService.getLoginUser();
        if(loginUserDTO == null){
            throw new ServiceException("用户令牌有误",ResultCode.INVALID_PARA.getCode());
        }
//      2.远程调用获取用户信息~~
        R<AppUserVO> result = appUserFeignClient.findById(loginUserDTO.getUserId());
        if(result == null || result.getCode() != ResultCode.SUCCESS.getCode()){
            throw new ServiceException("查询用户失败",ResultCode.INVALID_PARA.getCode());
        }

//      对象转换
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(loginUserDTO,userDTO);
        BeanUtils.copyProperties(result.getData(),userDTO);
        return userDTO;
    }


    /**
     * 退出登录
     */
    @Override
    public void logout() {

        String token = SecurityUtil.getToken();
        if(StringUtils.isEmpty(token)){
            return;
        }
        String userName = JwtUtil.getUserName(token);
        String userId = JwtUtil.getUserID(token);
        log.info("{}退出了系统,用户ID{}",userName,userId);
//      进行删除用户
        tokenService.delLoginUser(token);
    }

    /**
     * 处理用户登录逻辑
     * @param weChatLoginDTO
     * @param loginUserDTO
     */
    private void loginByWeChat(WeChatLoginDTO weChatLoginDTO,LoginUserDTO loginUserDTO){

        AppUserVO appUserVO;

//      1，根据openId来进行查询
        R<AppUserVO> result = appUserFeignClient.fingByOpenId(weChatLoginDTO.getOpenId());


        if(result == null || result.getCode() != ResultCode.SUCCESS.getCode() || result.getData() == null){
//           没查到需要进行注册
            appUserVO =  register(weChatLoginDTO);
        }else{
            appUserVO = result.getData();
        }
//      设置登录信息
        loginUserDTO.setUserId(appUserVO.getUserId());
        loginUserDTO.setUserName(appUserVO.getNickName());

    }

    /**
     * 根据入参来注册
     * @param loginDTO
     * @return 用户VO
     */
    private AppUserVO register(LoginDTO loginDTO){

        R<AppUserVO> result = null;
        if(loginDTO instanceof WeChatLoginDTO weChatLoginDTO){
//           进行注册
             result = appUserFeignClient.registerByOpenId(weChatLoginDTO.getOpenId());
            if(result == null || result.getCode() != ResultCode.SUCCESS.getCode() || result.getData() == null){
                log.error("用户注册失败！{}",weChatLoginDTO.getOpenId());
            }
        }else if(loginDTO instanceof CodeLoginDTO codeLoginDTO){
            result = appUserFeignClient.registerByPhone(codeLoginDTO.getPhone());
            if(result == null || result.getCode() != ResultCode.SUCCESS.getCode() || result.getData() == null){
                log.error("用户注册失败！{}",codeLoginDTO.getPhone());
            }

        }
        return result == null ? null : result.getData();
    }
}
