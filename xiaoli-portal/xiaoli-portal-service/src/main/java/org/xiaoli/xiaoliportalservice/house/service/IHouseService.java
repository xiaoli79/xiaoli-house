package org.xiaoli.xiaoliportalservice.house.service;

import org.xiaoli.xiaoliportalservice.house.domain.vo.HouseDataVO;

public interface IHouseService {

    /**
     * 获取房源详细信息
     * @param houseId
     * @return
     */
    HouseDataVO houseDetail(Long houseId);
}
