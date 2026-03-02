package org.xiaoli.xiaoliadminservice.user.controller;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.xiaoli.xiaoliadminservice.XiaoliAdminServiceApplication;
import org.xiaoli.xiaolicommoncore.utils.AESUtil;

@SpringBootTest(classes = XiaoliAdminServiceApplication.class)
public class AESTest {

    @Test
    void testEncrypt(){
        System.out.println(AESUtil.decryptHex("e64c5f44dc95e4ca77d99136ea2c88c6"));
    }
}
