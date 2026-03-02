package org.xiaoli.xiaoliadminservice.user.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xiaoli.xiaoliadminservice.XiaoliAdminServiceApplication;
import org.xiaoli.xiaolicommonsecurity.domain.dto.LoginUserDTO;
import org.xiaoli.xiaolicommonsecurity.service.TokenService;

@SpringBootTest(classes = XiaoliAdminServiceApplication.class)
public class TokenTest {

    @Autowired
    public TokenService tokenService;


    @Test
    public void tokenTest(){

        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUserId(100L);
        loginUserDTO.setUserName("xiaoli");
        loginUserDTO.setUserFrom("sys");
        System.out.println(tokenService.createToken(loginUserDTO));

    }
}
