package org.xiaoli.xiaoliadminservice.house.service;

import org.xiaoli.xiaoliadminservice.house.domain.dto.*;
import org.xiaoli.xiaolicommoncore.domain.dto.BasePageDTO;

import java.util.List;


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


    /**
     * 根据ID更新房源缓存
     * @param houseId
     */
    void cacheHouse(Long houseId);


    /**
     * 根据用户Id获取房源Id列表
     * @param userId
     * @return
     */
    List<Long> listByUserId(Long userId);




    /**
     * 刷新全量缓存
     * @return
     */
    void refreshHouseIds();
}
