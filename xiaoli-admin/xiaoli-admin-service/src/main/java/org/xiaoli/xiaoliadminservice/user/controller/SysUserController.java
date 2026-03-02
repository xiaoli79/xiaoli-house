package org.xiaoli.xiaoliadminservice.user.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiaoli.xiaoliadminservice.user.domain.dto.PasswordLoginDTO;
import org.xiaoli.xiaoliadminservice.user.domain.dto.SysUserDTO;
import org.xiaoli.xiaoliadminservice.user.domain.dto.SysUserListReqDTO;
import org.xiaoli.xiaoliadminservice.user.domain.vo.SysUserLoginVO;
import org.xiaoli.xiaoliadminservice.user.domain.vo.SysUserVO;
import org.xiaoli.xiaoliadminservice.user.service.ISysUserService;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.vo.TokenVO;
import org.xiaoli.xiaolicommonsecurity.domain.dto.TokenDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * B端用户服务控制器类
 */


@RestController
@RequestMapping("/sys_user")
public class SysUserController {




    @Autowired
    private ISysUserService loginService;
    @Autowired
    private ISysUserService iSysUserService;


    /**
     * B端用户登录
     * @param passwordLoginDTO 入参
     * @return
     */
    @PostMapping("/login")
    public R<TokenVO> login (@Validated @RequestBody PasswordLoginDTO passwordLoginDTO){
        TokenDTO tokenDTO = loginService.login(passwordLoginDTO);
        return R.ok(tokenDTO.convertTokenVO());
    }


    /**
     * B端用户新增或编辑用户
     * @param sysUserDTO
     * @return
     */
    @PostMapping("/add_edit")
    public R<Long> addOrEdit(@Validated @RequestBody SysUserDTO sysUserDTO){
        return R.ok(iSysUserService.addOrEdit(sysUserDTO));
    }

    /**
     * B端用户查询接口
     * @param sysUserListReqDTO
     * @return
     */
    @PostMapping("/list")
    public R<List<SysUserVO>> getUserList(@RequestBody SysUserListReqDTO sysUserListReqDTO){
        List<SysUserDTO> sysUserDTOS = iSysUserService.getUserList(sysUserListReqDTO);

        return R.ok(sysUserDTOS.stream()
                .map(SysUserDTO::convertToVO)
                .collect(Collectors.toList())
        );

    }


    @GetMapping("/login_info/get")
    public R<SysUserLoginVO> getLoginUser(){
        return R.ok(iSysUserService.getLoginUser().convertToVO());
    }

}
