package org.xiaoli.xiaoliadminapi.house.feign;


import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.xiaoli.xiaoliadminapi.house.domain.dto.SearchHouseListReqDTO;
import org.xiaoli.xiaoliadminapi.house.domain.vo.HouseDetailVO;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.vo.BasePageVO;

@FeignClient(contextId = "houseFeignClient",value = "xiaoli-admin",path = "/house")
public interface HouseFeignClient {


    /**
     * 查询房源列表，支持筛选、排序、翻页
     */
    @PostMapping("/list/search")
    R<BasePageVO<HouseDetailVO>> searchList(@Valid @RequestBody SearchHouseListReqDTO searchHouseReqDTO);

    /**
     * 查询房源详情（带缓存）
     */
    @GetMapping("/detail")
    R<HouseDetailVO> detail(@RequestParam Long houseId);


}
