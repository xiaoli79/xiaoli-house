package org.xiaoli.xiaoliportalservice.homepage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaoliportalservice.homepage.domain.vo.CityDescVO;
import org.xiaoli.xiaoliportalservice.homepage.service.IHomePageService;



@RestController
@RequestMapping("/homepage")
public class HomePageController {

    @Autowired
    private IHomePageService homePageService;


    /**
     * 根据经纬度来查询城市信息
     * @param lat
     * @param lng
     * @return
     */
    @GetMapping("/city_desc/get/nologin")
    public R<CityDescVO> getCityDesc(Double lat, Double lng){

        CityDescVO cityDescVO =  homePageService.getCityDesc(lat,lng);
        return R.ok(cityDescVO);
    }

}
