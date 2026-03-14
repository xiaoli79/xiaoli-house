package org.xiaoli.xiaoliportalservice.homepage.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.xiaoli.xiaoliadminapi.map.domain.dto.LocationReqDTO;
import org.xiaoli.xiaoliadminapi.map.domain.vo.RegionCityVO;
import org.xiaoli.xiaoliadminapi.map.feign.MapFeignClient;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;
import org.xiaoli.xiaoliportalservice.homepage.domain.vo.CityDescVO;
import org.xiaoli.xiaoliportalservice.homepage.service.IHomePageService;


@Service
public class HomePageServiceImpl implements IHomePageService {


    @Autowired
    private MapFeignClient mapFeignClient;

    /**
     * 根据经纬度来查询城市信息
     * @param lat
     * @param lng
     * @return
     */
    @Override
    public CityDescVO getCityDesc(Double lat, Double lng) {
        if(null==lat||null==lng){
            throw new ServiceException("经纬度有一个或两个为空");
        }

        LocationReqDTO locationReqDTO = new LocationReqDTO();
        locationReqDTO.setLat(lat);
        locationReqDTO.setLng(lng);
//      设置其值
        R<RegionCityVO> result = mapFeignClient.locateCityByLocation(locationReqDTO);

        if(null == result|| result.getCode() != ResultCode.SUCCESS.getCode() || null == result.getData()) {
            throw new ServiceException("根据定位获取城市信息失败!!!");
        }

        CityDescVO cityDescVO = new CityDescVO();
        BeanUtils.copyProperties(result.getData(),cityDescVO);
        return cityDescVO;

    }
}
