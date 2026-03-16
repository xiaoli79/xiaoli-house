package org.xiaoli.xiaoliadminservice.house.service.filter;

import org.xiaoli.xiaoliadminapi.house.domain.dto.SearchHouseListReqDTO;
import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseDTO;

public interface IHouseFilter {


    /**
     * 过滤房源
     * @param houseDTO
     * @param reqDTO
     * @return
     */
    Boolean filter(HouseDTO houseDTO, SearchHouseListReqDTO reqDTO);
}
