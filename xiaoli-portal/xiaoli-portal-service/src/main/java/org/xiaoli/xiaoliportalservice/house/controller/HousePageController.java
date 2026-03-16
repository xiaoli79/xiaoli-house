package org.xiaoli.xiaoliportalservice.house.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaoliportalservice.house.domain.vo.HouseDataVO;
import org.xiaoli.xiaoliportalservice.house.service.IHouseService;

@RestController
@RequestMapping("/housepage")
public class HousePageController {



    @Autowired
    private IHouseService houseService;


    /**
     * 获取房源详细信息
     * @param houseId
     * @return
     */
    @GetMapping("/get/nologin")
    public R<HouseDataVO> houseDetail (Long houseId){
        return R.ok(houseService.houseDetail(houseId));
    }

}
