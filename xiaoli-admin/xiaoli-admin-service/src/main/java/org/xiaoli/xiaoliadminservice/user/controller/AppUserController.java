package org.xiaoli.xiaoliadminservice.user.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaoli.xiaoliadminapi.appUser.domain.dto.AppUserDTO;
import org.xiaoli.xiaoliadminapi.appUser.domain.dto.AppUserListReqDTO;
import org.xiaoli.xiaoliadminapi.appUser.domain.dto.UserEditReqDTO;
import org.xiaoli.xiaoliadminapi.appUser.domain.vo.AppUserVO;
import org.xiaoli.xiaoliadminapi.appUser.feign.AppUserFeignClient;
import org.xiaoli.xiaoliadminservice.user.service.IAppUserService;
import org.xiaoli.xiaolicommoncore.domain.dto.BasePageDTO;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.vo.BasePageVO;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * C端用户相关接口实现
 */
@RestController
@RequestMapping("/app_user")
public class AppUserController implements AppUserFeignClient {


    @Autowired
    private IAppUserService appUserService;

    /**
     * 根据微信注册用户
     * @param openId 用户微信ID
     * @return C端用户VO
     */
    @Override
    public R<AppUserVO> registerByOpenId(String openId) {
        AppUserDTO appUserDTO = appUserService.registerByOpenId(openId);
        return R.ok(appUserDTO.convertToVO());
    }


    /**
     * 根据openid来查询用户信息
     * @param openId 用户微信ID
     * @return C端用户VO
     */
    @Override
    public R<AppUserVO> fingByOpenId(String openId) {

        AppUserDTO appUserDTO = appUserService.findByOpenId(openId);
        if (appUserDTO == null) {
            return R.ok();
        }
        return R.ok(appUserDTO.convertToVO());
    }



    /**
     * 根据手机号来查询用户信息
     * @param phoneNumber
     * @return
     */
    @Override
    public R<AppUserVO> findByPhone(String phoneNumber) {
        AppUserDTO appUserDTO = appUserService.findByPhone(phoneNumber);
        if(appUserDTO == null){
            return R.ok();
        }
        return R.ok(appUserDTO.convertToVO());
    }


    /**
     * 根据手机号来注册用户信息
     * @param phoneNumber
     * @return
     */
    @Override
    public R<AppUserVO> registerByPhone(String phoneNumber) {
        AppUserDTO appUserDTO = appUserService.registerByPhone(phoneNumber);
        if(appUserDTO == null){
            throw new ServiceException("注册失败");
        }
        return R.ok(appUserDTO.convertToVO());
    }

    /**
     * 编辑C端用户
     * @param userEditReqDTO C段用户DTO
     * @return
     */
    @Override
    public R<Void> edit(UserEditReqDTO userEditReqDTO) {
       appUserService.edit(userEditReqDTO);
       return R.ok();
    }


    /**
     * 根据用户ID来查询用户信息
     * @param userId
     * @return
     */
    @Override
    public R<AppUserVO> findById(Long userId) {
        AppUserDTO appUserDTO =  appUserService.findById(userId);
        return  R.ok(appUserDTO.convertToVO());
    }


    /**
     * 根据用户ID列表获取用户列表信息
     * @param userIds
     * @return
     */
    @Override
    public R<List<AppUserVO>> list(List<Long> userIds) {



        List<AppUserDTO> appUserDTOList = appUserService.getUserList(userIds);


        return R.ok(appUserDTOList.stream()
                .filter(Objects::nonNull)
                .map(AppUserDTO::convertToVO)
                .collect(Collectors.toList())
        );
    }


    /**
     * 查询C端用户
     * @param appUserListReqDTO 查询C端用户DTO
     * @return C端用户分页结果
     */
    @PostMapping("/list/search")

    public R<BasePageVO<AppUserVO>> list(@RequestBody AppUserListReqDTO appUserListReqDTO){

        BasePageDTO<AppUserDTO> appUserDTOList = appUserService.getUserList(appUserListReqDTO);

        BasePageVO<AppUserVO> result = new BasePageVO<>();

        BeanUtils.copyProperties(appUserDTOList,result);

        return R.ok(result);

    }






}
