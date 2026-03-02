package org.xiaoli.xiaoliadminservice.user.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xiaoli.xiaoliadminapi.appUser.domain.dto.UserEditReqDTO;




@SpringBootTest
class AppUserServiceImplTest {



    @Autowired
    AppUserServiceImpl appUserServiceImpl;

    @Test
    void edit() {

        UserEditReqDTO userEditReqDTO = new UserEditReqDTO();
        userEditReqDTO.setUserId(10000008L);
        userEditReqDTO.setNickName("xiaoli");
        userEditReqDTO.setAvatar("666");
        appUserServiceImpl.edit(userEditReqDTO);
    }
}