package org.xiaoli.xiaoliportalservice.homepage.service;


import org.springframework.stereotype.Service;
import org.xiaoli.xiaoliportalservice.homepage.domain.vo.CityDescVO;



public interface IHomePageService {

    /**
     * 根据经纬度来查询城市信息
     * @param lat
     * @param lng
     * @return
     */
    CityDescVO getCityDesc(Double lat, Double lng);
}
