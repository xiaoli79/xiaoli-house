package org.xiaoli.xiaoliportalservice.homepage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaoliportalservice.homepage.domain.dto.PullDataListReqDTO;
import org.xiaoli.xiaoliportalservice.homepage.domain.vo.CityDescVO;
import org.xiaoli.xiaoliportalservice.homepage.domain.vo.PullDataListVO;
import org.xiaoli.xiaoliportalservice.homepage.service.IHomePageService;



@RestController
@RequestMapping("/homepage")
public class HomePageController {

    @Autowired
    private IHomePageService homePageService;


    /**
     * 根据经纬度来查询城市信息
     *
     * @param lat
     * @param lng
     * @return
     */
    @GetMapping("/city_desc/get/nologin")
    public R<CityDescVO> getCityDesc(Double lat, Double lng) {

        CityDescVO cityDescVO = homePageService.getCityDesc(lat, lng);
        return R.ok(cityDescVO);
    }


    /**
     * 获取下拉筛选数据列表
     * @param pullDataListReqDTO
     * @return
     */
    @PostMapping("/pull_list/get/nologin")
    public R<PullDataListVO> getPullData(@Validated @RequestBody PullDataListReqDTO pullDataListReqDTO) {


        return R.ok(homePageService.getPullData(pullDataListReqDTO));


    }
}


