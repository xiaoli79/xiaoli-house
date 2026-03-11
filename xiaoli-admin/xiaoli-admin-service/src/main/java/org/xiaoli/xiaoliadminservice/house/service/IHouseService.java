package org.xiaoli.xiaoliadminservice.house.service;

import org.xiaoli.xiaoliadminservice.house.domain.dto.*;
import org.xiaoli.xiaolicommoncore.domain.dto.BasePageDTO;


public interface IHouseService {


    Long addOrEdit(HouseAddOrEditReqDTO houseAddOrEditReqDTO);




    /**
     * 查询房源相关的信息
     * @param houseId
     * @return
     */
    HouseDTO detail(Long houseId);



    /**
     * 查询房源摘要列表
     * @param houseListReqDTO
     * @return
     */
    BasePageDTO<HouseDescDTO> list(HouseListReqDTO houseListReqDTO);


    /**
     * 房源状态修改
     * @param houseStatusEditReqDTO
     * @return
     */
    void editStatus(HouseStatusEditReqDTO houseStatusEditReqDTO);
}
