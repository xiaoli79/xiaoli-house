package org.xiaoli.xiaoliadminservice.house.service;


import org.springframework.stereotype.Service;
import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseAddOrEditReqDTO;

@Service
public interface IHouseService {


    Long addOrEdit(HouseAddOrEditReqDTO houseAddOrEditReqDTO);
}
