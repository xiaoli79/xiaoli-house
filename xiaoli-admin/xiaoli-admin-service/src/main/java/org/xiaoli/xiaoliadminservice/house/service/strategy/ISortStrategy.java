package org.xiaoli.xiaoliadminservice.house.service.strategy;

import org.xiaoli.xiaoliadminapi.house.domain.dto.SearchHouseListReqDTO;
import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseDTO;

import java.util.List;

public interface ISortStrategy {


    /**
     * 排序
     * @param houseDTOList
     * @param reqDTO
     * @return
     */
    List<HouseDTO> sort(List<HouseDTO> houseDTOList, SearchHouseListReqDTO reqDTO);
}
