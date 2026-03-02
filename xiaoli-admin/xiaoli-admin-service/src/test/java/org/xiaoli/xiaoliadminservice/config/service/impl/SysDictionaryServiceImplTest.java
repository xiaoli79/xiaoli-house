package org.xiaoli.xiaoliadminservice.config.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.xiaoli.xiaoliadminapi.config.domain.dto.DictionaryDataDTO;
import org.xiaoli.xiaoliadminapi.config.domain.dto.DictionaryTypeListReqDTO;
import org.xiaoli.xiaoliadminapi.config.domain.dto.DictionaryTypeWriteReqDTO;
import org.xiaoli.xiaoliadminservice.config.service.ISysDictionaryService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@SpringBootTest
class SysDictionaryServiceImplTest {


    @Autowired
    private ISysDictionaryService sysDictionaryService;

    @Test
//  加上事务回滚~~
    @Transactional
    void addType() {

        DictionaryTypeWriteReqDTO dto = new DictionaryTypeWriteReqDTO();
        dto.setTypeKey("weight");
        dto.setValue("重量");
        dto.setRemark("重量配置");
        Assertions.assertTrue(sysDictionaryService.addType(dto) > 0L);
        ;
    }

    @Test
    @Transactional
    void listType() {

        DictionaryTypeListReqDTO dto = new DictionaryTypeListReqDTO();
        dto.setTypeKey("weight");
        dto.setValue("重");

        System.out.println(sysDictionaryService.listType(dto));


//      不加参数进行查询
//        DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO = new DictionaryTypeWriteReqDTO();
//        System.out.println(sysDictionaryService.addType(dictionaryTypeWriteReqDTO));


    }

    /**
     * 测试获取多个字典类型下的所有字典数据
     * 对应接口: POST /admin/dictionary_data/types
     * 请求体示例:
     * [
     *     "user_gender",
     *     "order_status",
     *     "sys_priority"
     * ]
     */
    @Test
    void selecDataByTypes() {
        // 1. 构建请求参数 - 多个字典类型键
        List<String> typeKeys = Arrays.asList(
                "user_gender",    // 用户性别
                "order_status",   // 订单状态
                "sys_priority"    // 系统优先级
        );

        // 2. 调用服务方法
        Map<String, List<DictionaryDataDTO>> result = sysDictionaryService.selecDataByTypes(typeKeys);

        // 3. 打印返回结果
        System.out.println("查询结果: " + result);

        // 4. 断言：返回结果不为空
        Assertions.assertNotNull(result, "返回结果不应为空");

        // 5. 遍历结果，打印每个字典类型的数据
        result.forEach((key, value) -> {
            System.out.println("字典类型: " + key);
            System.out.println("字典数据列表: " + value);
            System.out.println("-------------------");
        });
    }
}