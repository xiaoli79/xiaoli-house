package org.xiaoli.xiaoliportalservice.house.service.impl;


import jodd.bean.BeanCopy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoli.xiaoliadminapi.house.domain.vo.HouseDetailVO;
import org.xiaoli.xiaoliadminapi.house.feign.HouseFeignClient;
import org.xiaoli.xiaolicommoncore.utils.BeanCopyUtil;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaoliportalservice.house.domain.vo.HouseDataVO;
import org.xiaoli.xiaoliportalservice.house.service.IHouseService;

@Service
@Slf4j
public class HouseServiceImpl implements IHouseService {


    @Autowired
    private HouseFeignClient houseFeignClient;

    /**
     * 获取房源详细信息
     * @param houseId
     * @return
     */
    @Override
    public HouseDataVO houseDetail(Long houseId) {

        if(null  == houseId){
            log.error("传入的房源Id为空");
            return null;
        }

        R<HouseDetailVO> detail = houseFeignClient.detail(houseId);
        if( null == detail || detail.getCode() != ResultCode.SUCCESS.getCode()  ||  null == detail.getData() ){
            log.error("根据房源Id，没有查到房源详细信息");
        }
        HouseDataVO houseDataVO = new HouseDataVO();
        BeanCopyUtil.copyProperties(detail.getData(), houseDataVO);
        return houseDataVO;
    }
}
