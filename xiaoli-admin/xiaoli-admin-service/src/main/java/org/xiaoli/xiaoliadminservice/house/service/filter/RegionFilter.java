package org.xiaoli.xiaoliadminservice.house.service.filter;

import org.springframework.stereotype.Component;
import org.xiaoli.xiaoliadminapi.house.domain.dto.SearchHouseListReqDTO;
import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseDTO;


/**
 * 区域筛选策略
 */


@Component
public class RegionFilter implements IHouseFilter {
    @Override
    public Boolean filter(HouseDTO houseDTO, SearchHouseListReqDTO reqDTO) {
        //1.不设置区域筛选条件
        //2.传递的区域筛选条件与房源的所在区一致
        return null == reqDTO.getRegionId() || houseDTO.getRegionId().equals(reqDTO.getRegionId());
    }
}
