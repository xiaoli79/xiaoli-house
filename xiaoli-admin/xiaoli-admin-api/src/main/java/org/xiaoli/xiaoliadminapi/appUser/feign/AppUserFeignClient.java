package org.xiaoli.xiaoliadminapi.appUser.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.xiaoli.xiaoliadminapi.appUser.domain.dto.UserEditReqDTO;
import org.xiaoli.xiaoliadminapi.appUser.domain.vo.AppUserVO;
import org.xiaoli.xiaolicommondomain.domain.R;

import java.util.List;

/**
 * C端用户数据操作的远程调用
 */
@FeignClient(contextId = "appUserFeignClient",value="xiaoli-admin",path = "/app_user")
public interface AppUserFeignClient {


    /**
     * 根据微信注册用户
     * @param openId 用户微信ID
     * @return C端用户VO
     */
    @GetMapping("/register/openid")
    R<AppUserVO> registerByOpenId (@RequestParam String openId);


    /**
     * 根据openid来查询用户信息
     * @param openId 用户微信ID
     * @return C端用户VO
     */
    @GetMapping("/open_id_find")
    R<AppUserVO> fingByOpenId (@RequestParam String openId);


    /**
     * 根据手机号来查询用户信息
     * @param phoneNumber
     * @return
     */
    @GetMapping("/phone_find")
    R<AppUserVO>  findByPhone(@RequestParam String phoneNumber);



    /**
     * 根据手机号来注册用户信息
     * @param phoneNumber
     * @return
     */
    @GetMapping("/register/phone")
    R<AppUserVO>  registerByPhone(@RequestParam String phoneNumber);


    /**
     * 编辑C端用户
     * @param userEditReqDTO C段用户DTO
     * @return
     */
    @PostMapping("/edit")
    R<Void> edit(@RequestBody @Validated UserEditReqDTO userEditReqDTO);


    /**
     * 根据用户ID获取用户信息
     * @param userId
     * @return C端用户VO
     */
    @GetMapping("/id_find")
    R<AppUserVO>  findById(@RequestParam Long userId);


    /**
     * 根据用户ID列表获取用户列表信息
     * @param userIds
     * @return
     */
    @PostMapping("/list")
    R<List<AppUserVO>> list(@RequestBody List<Long> userIds);








}
