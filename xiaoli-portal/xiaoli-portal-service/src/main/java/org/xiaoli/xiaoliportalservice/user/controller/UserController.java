package org.xiaoli.xiaoliportalservice.user.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiaoli.xiaoliadminapi.appUser.domain.dto.UserEditReqDTO;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.vo.TokenVO;
import org.xiaoli.xiaoliportalservice.user.entity.dto.CodeLoginDTO;
import org.xiaoli.xiaoliportalservice.user.entity.dto.WeChatLoginDTO;
import org.xiaoli.xiaoliportalservice.user.entity.vo.UserVo;
import org.xiaoli.xiaoliportalservice.user.service.IUserService;

/**
 * 门户程序用户的入口
 */

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {



    @Autowired
    IUserService userService;


    /**
     * 微信登录
     * @param weChatLoginDTO 微信登录DTO
     * @return
     */
    @PostMapping("/login/wechat")
    public R<TokenVO>  login (@RequestBody @Validated WeChatLoginDTO weChatLoginDTO){

        return R.ok(userService.login(weChatLoginDTO).convertTokenVO());
    }


    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @GetMapping("/send_code")
    public R<Boolean> sendCode(String phone){
        return R.ok(userService.sendCode(phone));
    }


    /**
     * 验证码登录
     * @param codeLoginDTO
     * @return
     */
    @PostMapping("/login/code")
    public R<TokenVO> login (@RequestBody @Validated CodeLoginDTO codeLoginDTO){
        return R.ok(userService.login(codeLoginDTO).convertTokenVO());

    }

    /**
     * 编辑C端用户
     * @param userEditReqDTO C段用户DTO
     * @return
     */
    @PostMapping("/edit")
    public R<Void> edit(@RequestBody @Validated UserEditReqDTO userEditReqDTO){
        userService.edit(userEditReqDTO);
        return R.ok();
    }


    /**
     * 获取C端登录用户信息
     * @return
     */
    @GetMapping("/login_info/get")
    public R<UserVo> getLoginuser(){
        return R.ok(userService.getLoginUser().convertToVO());
    }

    /**
     * 退出登录
     * @return
     */
    @DeleteMapping("/logout")
    R<Void> logout(){
        userService.logout();
        return R.ok();
    }

}
