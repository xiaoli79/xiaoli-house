package org.xiaoli.xiaoliportalservice.homepage.service;


import org.springframework.stereotype.Service;
import org.xiaoli.xiaolicommondomain.domain.vo.BasePageVO;
import org.xiaoli.xiaoliportalservice.homepage.domain.dto.HouseListReqDTO;
import org.xiaoli.xiaoliportalservice.homepage.domain.dto.PullDataListReqDTO;
import org.xiaoli.xiaoliportalservice.homepage.domain.vo.CityDescVO;
import org.xiaoli.xiaoliportalservice.homepage.domain.vo.HouseDescVO;
import org.xiaoli.xiaoliportalservice.homepage.domain.vo.PullDataListVO;


public interface IHomePageService {

    /**
     * 根据经纬度来查询城市信息
     * @param lat
     * @param lng
     * @return
     */
    CityDescVO getCityDesc(Double lat, Double lng);


    /**
     * 获取下拉筛选数据列表
     * @param pullDataListReqDTO
     * @return
     */
    PullDataListVO getPullData(PullDataListReqDTO pullDataListReqDTO);


    /**
     * 查询房源列表
     * @param houseListReqDTO
     * @return
     */
    BasePageVO<HouseDescVO> houseList(HouseListReqDTO houseListReqDTO);
}
