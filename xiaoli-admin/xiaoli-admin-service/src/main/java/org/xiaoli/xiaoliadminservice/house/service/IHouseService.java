package org.xiaoli.xiaoliadminservice.house.service;

import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseAddOrEditReqDTO;
import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseDTO;
import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseDetailDTO;


public interface IHouseService {


    Long addOrEdit(HouseAddOrEditReqDTO houseAddOrEditReqDTO);




    /**
     * 查询房源相关的信息
     * @param houseId
     * @return
     */
    HouseDTO detail(Long houseId);
}
