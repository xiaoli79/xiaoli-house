package org.xiaoli.xiaoliadminservice.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xiaoli.xiaoliadminapi.appUser.domain.dto.AppUserDTO;
import org.xiaoli.xiaoliadminapi.appUser.domain.dto.AppUserListReqDTO;
import org.xiaoli.xiaoliadminapi.appUser.domain.dto.UserEditReqDTO;
import org.xiaoli.xiaoliadminservice.user.config.RabbitConfig;
import org.xiaoli.xiaoliadminservice.user.domain.entity.AppUser;
import org.xiaoli.xiaoliadminservice.user.domain.entity.SysUser;
import org.xiaoli.xiaoliadminservice.user.mapper.AppUserMapper;
import org.xiaoli.xiaoliadminservice.user.service.IAppUserService;
import org.xiaoli.xiaolicommoncore.domain.dto.BasePageDTO;
import org.xiaoli.xiaolicommoncore.utils.AESUtil;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class AppUserServiceImpl implements IAppUserService {


    @Autowired
    private AppUserMapper appUserMapper;

    @Value("${appuser.info.defaultAvatar}")
    private String defaultAvatar;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 根据微信注册用户
     * @param openId 用户微信ID
     * @return C端用户VO
     */
    @Override
    public AppUserDTO registerByOpenId(String openId) {

        /**
         * 判空逻辑
         */
        if(StringUtils.isEmpty(openId)){
            throw new ServiceException("微信ID不能为空", ResultCode.INVALID_PARA.getCode());
        }
//      进行设置默认值
        AppUser appUser = new AppUser();
        appUser.setOpenId(openId);
        appUser.setAvatar(defaultAvatar);
        appUser.setNickName("肥波的忠实粉"+(int)(Math.random()*9000) + 1000);
        appUser.setPhoneNumber(null);
//      执行插入逻辑
        appUserMapper.insert(appUser);
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser,appUserDTO);
        appUserDTO.setUserId(appUser.getId());
        appUserDTO.setOpenId(openId);

        return appUserDTO;
    }



    /**
     * 根据openid来查询用户信息
     * @param openId 用户微信ID
     * @return C端用户VO
     */
    @Override
    public AppUserDTO findByOpenId(String openId) {

        if(StringUtils.isEmpty(openId)){
            return null;
        }

        AppUser appUser = appUserMapper.selectOne(new LambdaQueryWrapper<AppUser>().eq(AppUser::getOpenId, openId));

        if(appUser == null){
            return null;
        }

        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser,appUserDTO);
        appUserDTO.setUserId(appUser.getId());
//      处理手机号~~
        appUserDTO.setPhoneNumber(AESUtil.encryptHex(appUser.getPhoneNumber()));
        return appUserDTO;
    }


    /**
     * 根据手机号来查询用户信息
     * @param phoneNumber
     * @return
     */
    @Override
    public AppUserDTO findByPhone(String phoneNumber) {
        if(StringUtils.isEmpty(phoneNumber)){
            return null;
        }

       AppUser appUser= appUserMapper.selectOne(new LambdaQueryWrapper<AppUser>().eq(AppUser::getPhoneNumber,AESUtil.encryptHex(phoneNumber)));
        if(appUser == null){
            return null;
        }
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser,appUserDTO);
        appUserDTO.setUserId(appUser.getId());
        appUserDTO.setPhoneNumber(AESUtil.decryptHex(appUser.getPhoneNumber()));
        return appUserDTO;
    }

    /**
     * 根据手机号来注册用户信息
     * @param phoneNumber
     * @return
     */
    @Override
    public AppUserDTO registerByPhone(String phoneNumber) {

        /**
         * 判空逻辑
         */
        if(StringUtils.isEmpty(phoneNumber)){
            throw new ServiceException("手机号不能为空", ResultCode.INVALID_PARA.getCode());
        }
//      进行设置默认值
        AppUser appUser = new AppUser();
        appUser.setAvatar(defaultAvatar);
        appUser.setNickName("肥波的忠实粉"+(int)(Math.random()*9000) + 1000);
        appUser.setPhoneNumber(AESUtil.encryptHex(phoneNumber));
//      执行插入逻辑
        appUserMapper.insert(appUser);
        AppUserDTO  appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser,appUserDTO);
        appUserDTO.setUserId(appUser.getId());
        return appUserDTO;
    }


    /**
     * 编辑C端用户
     * @param userEditReqDTO C段用户DTO
     * @return
     */
    @Override
    public void edit(UserEditReqDTO userEditReqDTO) {
        AppUser appUser = appUserMapper.selectById(userEditReqDTO.getUserId());
        if(appUser == null){
            throw new ServiceException("用户不存在",ResultCode.INVALID_PARA.getCode());
        }

        appUser.setNickName(userEditReqDTO.getNickName());
        appUser.setAvatar(userEditReqDTO.getAvatar());
        appUserMapper.updateById(appUser);

//      发送广播消息
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser,appUserDTO);
        appUserDTO.setUserId(appUser.getId());
        try{
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME,"",appUserDTO);
//                                        交换机名称                    广播模式      消息内容：用户信息
        }catch (Exception e){
            log.error("编辑用户发送消息失败",e);
        }


    }


    /**
     * 查询C端用户
     * @param appUserListReqDTO 查询C端用户DTO
     * @return C端用户分页结果
     */
    @Override
    public BasePageDTO<AppUserDTO> getUserList(AppUserListReqDTO appUserListReqDTO) {
        
//      先给手机号进行加密
        if (StringUtils.isNotBlank(appUserListReqDTO.getPhoneNumber())) {
            appUserListReqDTO.setPhoneNumber(AESUtil.encryptHex(appUserListReqDTO.getPhoneNumber()));
        }
        
        BasePageDTO<AppUserDTO> result = new BasePageDTO<>();
        
        
//      计算总数
        Long totals = appUserMapper.selectCount(appUserListReqDTO);
        if(totals==0){
            result.setTotals(0);
            result.setTotalPages(0);
            result.setList(new ArrayList<>());
            return result;
        }
        // 3 分页查询
        List<AppUser> appUserList = appUserMapper.selectPage(appUserListReqDTO);
        result.setTotals(totals.intValue());
        result.setTotalPages(
                BasePageDTO.calculateTotalPages(totals, appUserListReqDTO.getPageSize())
        );
        // 4 超页
        if (CollectionUtils.isEmpty(appUserList)) {
            result.setList(new ArrayList<>());
            return result;
        }
        // 5 对象列表结果转换
        result.setList(
                appUserList.stream()
                        .map(appUser -> {
                            AppUserDTO appUserDTO = new AppUserDTO();
                            BeanUtils.copyProperties(appUser, appUserDTO);
                            appUserDTO.setUserId(appUser.getId());
                            appUserDTO.setPhoneNumber(AESUtil.decryptHex(appUser.getPhoneNumber()));
                            return appUserDTO;
                        }).collect(Collectors.toList())
        );
        return result;


    }

    @Override
    public AppUserDTO findById(Long userId) {


        if(userId == null){
            return null;
        }

        AppUser appUser = appUserMapper.selectById(userId);
        if(appUser == null){
            return null;
        }
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser,appUserDTO);
        appUserDTO.setPhoneNumber(AESUtil.decryptHex(appUser.getPhoneNumber()));
        appUserDTO.setUserId(appUser.getId());
        return appUserDTO;

    }


    @Override
    public List<AppUserDTO> getUserList(List<Long> userIds){


//      判空
        if(CollectionUtils.isEmpty(userIds)){
            return Arrays.asList();
        }

//      查询数据库
        List<AppUser> appUsers = appUserMapper.selectBatchIds(userIds);


//      对象转换
        return appUsers.stream()
               .map(appUser ->  {
                   AppUserDTO appUserDTO = new AppUserDTO();
                   BeanUtils.copyProperties(appUser,appUserDTO);
                   appUserDTO.setPhoneNumber(AESUtil.encryptHex(appUser.getPhoneNumber()));
                   appUserDTO.setUserId(appUser.getId());
                   return appUserDTO;
               }).collect(Collectors.toList());

    }


}
