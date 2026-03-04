package org.xiaoli.xiaoliadminservice.house.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseAddOrEditReqDTO;
import org.xiaoli.xiaoliadminservice.house.service.IHouseService;
import org.xiaoli.xiaolicommondomain.domain.R;

@RestController
@RequestMapping("/house")
public class HouseController {




    @Autowired
    IHouseService houseService;


    @PostMapping("add_edit")
    public R<Long> addOrEdit(HouseAddOrEditReqDTO houseAddOrEditReqDTO){

        Long houseId = houseService.addOrEdit(houseAddOrEditReqDTO);
        return R.ok(houseId);

    }


}
