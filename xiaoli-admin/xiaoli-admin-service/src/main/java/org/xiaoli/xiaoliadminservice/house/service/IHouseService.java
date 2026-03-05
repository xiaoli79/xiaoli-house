package org.xiaoli.xiaoliadminservice.house.service;

import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseAddOrEditReqDTO;




public interface IHouseService {


    Long addOrEdit(HouseAddOrEditReqDTO houseAddOrEditReqDTO);
}
