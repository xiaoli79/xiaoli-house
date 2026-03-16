package org.xiaoli.xiaoliadminservice.house.service.filter;


import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.xiaoli.xiaoliadminapi.house.domain.dto.SearchHouseListReqDTO;
import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseDTO;


/**
 * 出租类型策略
 */
@Component
public class RentTypeFilter implements IHouseFilter{

    @Override
    public Boolean filter(HouseDTO houseDTO, SearchHouseListReqDTO reqDTO) {
        return CollectionUtils.isEmpty(reqDTO.getRentTypes()) || reqDTO.getRentTypes().contains(houseDTO.getRentType());
    }

}
