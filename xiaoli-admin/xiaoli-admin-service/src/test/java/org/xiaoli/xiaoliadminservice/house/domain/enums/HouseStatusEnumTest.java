package org.xiaoli.xiaoliadminservice.house.domain.enums;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class HouseStatusEnumTest {

    @Test
    void getEnumByName() {

        System.out.println(HouseStatusEnum.getEnumByName("up").getMsg());

    }
}