package org.xiaoli.xiaoliadminservice.map.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xiaoli.xiaoliadminservice.map.domain.dto.PoiListDTO;
import org.xiaoli.xiaoliadminservice.map.domain.dto.SuggestSearchDTO;
import org.xiaoli.xiaoliadminservice.map.service.IMapProvider;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QQMapServiceImplTest {

    @Autowired
    private IMapProvider mapProvider;

    @Test
    void searchQQMapPlaceByRegion() {
        // 1. 构建请求参数
        SuggestSearchDTO dto = new SuggestSearchDTO();
        dto.setKeyword("餐厅");       // 搜索关键字
        dto.setId("110000");          // 北京的 adcode
        dto.setPageIndex(1);          // 第一页
        dto.setPageSize(10);          // 每页10条

        // 2. 调用接口
        PoiListDTO result = mapProvider.searchQQMapPlaceByRegion(dto);

        // 3. 打印结果
        System.out.println("查询结果: " + result);

        // 4. 断言：结果不为空
        assertNotNull(result, "返回结果不应为空");
    }
}