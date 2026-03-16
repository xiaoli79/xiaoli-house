package org.xiaoli.xiaoliadminservice.house.service.filter;

import org.springframework.stereotype.Component;
import org.xiaoli.xiaoliadminapi.house.domain.dto.SearchHouseListReqDTO;
import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseDTO;
import org.xiaoli.xiaoliadminservice.house.domain.enums.HouseStatusEnum;


/**
 * 对房源状态的过滤
 */
@Component
public class StatusFilter implements IHouseFilter {
    @Override
    public Boolean filter(HouseDTO houseDTO, SearchHouseListReqDTO reqDTO) {
        return houseDTO.getStatus().equalsIgnoreCase(HouseStatusEnum.UP.name());
    }
}
