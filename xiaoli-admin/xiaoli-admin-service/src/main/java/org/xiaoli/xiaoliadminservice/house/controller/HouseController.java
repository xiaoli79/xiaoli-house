package org.xiaoli.xiaoliadminservice.house.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiaoli.xiaoliadminservice.house.domain.dto.*;
import org.xiaoli.xiaoliadminservice.house.domain.vo.HouseDetailVO;
import org.xiaoli.xiaoliadminservice.house.domain.vo.HouseVO;
import org.xiaoli.xiaoliadminservice.house.service.IHouseService;
import org.xiaoli.xiaolicommoncore.domain.dto.BasePageDTO;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.vo.BasePageVO;


@Slf4j
@RestController
@RequestMapping("/house")
public class HouseController {


    @Autowired
    private IHouseService houseService;


    @PostMapping("add_edit")
    public R<Long> addOrEdit(@Validated @RequestBody HouseAddOrEditReqDTO houseAddOrEditReqDTO){

        Long houseId = houseService.addOrEdit(houseAddOrEditReqDTO);
        return R.ok(houseId);

    }


    /**
     * 查询房源相关的信息
     * @param houseId
     * @return
     */
    @GetMapping("/detail")
    public R<HouseDetailVO> detail(@RequestParam @Validated Long houseId){

        HouseDTO houseDTO =  houseService.detail(houseId);

        if(null == houseDTO){
            log.warn("要查询的房源不存在，houseId:{}",houseId);
            return R.fail("房源详情不存在");
        }

        return R.ok(houseDTO.convertToVO());
    }


    /**
     * 查询房源摘要列表
     * @param houseListReqDTO
     * @return
     */
    @PostMapping("/list")
    public R<BasePageVO<HouseVO>> list(@RequestBody @Validated HouseListReqDTO houseListReqDTO){
        BasePageDTO<HouseDescDTO> houseDescList = houseService.list(houseListReqDTO);
        BasePageVO<HouseVO> result = new  BasePageVO<>();
        BeanUtils.copyProperties(houseDescList,result);
        return R.ok(result);
    }

}
