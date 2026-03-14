package org.xiaoli.xiaoliportalservice.city.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaoliportalservice.city.domain.vo.CityPageVO;
import org.xiaoli.xiaoliportalservice.city.service.ICityService;

@RestController
@RequestMapping("/citypage")
public class CityPageController {

    @Autowired
    private ICityService cityService;


    /**
     * 获取热门城市以及A-Z城市列表
     * @return
     */
    @GetMapping("/get/nologin")
    public R<CityPageVO> cityPage(){
        return R.ok(cityService.getCityPage());
    }









}
