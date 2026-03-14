package org.xiaoli.xiaoliportalservice.city.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.xiaoli.xiaoliadminapi.map.domain.vo.RegionVO;
import org.xiaoli.xiaoliadminapi.map.feign.MapFeignClient;
import org.xiaoli.xiaolicommoncore.utils.BeanCopyUtil;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaoliportalservice.city.domain.vo.CityPageVO;
import org.xiaoli.xiaoliportalservice.city.service.ICityService;
import org.xiaoli.xiaoliportalservice.homepage.domain.vo.CityDescVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


/**
 * 异步线程~~
 */
@Slf4j
@Service
public class CityServiceImpl implements ICityService {

    @Autowired
    private Executor threadPoolTaskExecutor;

    @Autowired
    private MapFeignClient mapFeignClient;

    @Override
    public CityPageVO getCityPage() {

        //1.异步编排
        CompletableFuture<List<CityDescVO>> hotCityListFuture = CompletableFuture.supplyAsync(
                this::getHotCityList,threadPoolTaskExecutor);

        CompletableFuture<Map<String,List<CityDescVO>>> cityPyMapFuture  = CompletableFuture.supplyAsync(
                this::getCityPyMap,threadPoolTaskExecutor);

        //2.查询热门城市列表
        CompletableFuture<Void> completableFuture =
                CompletableFuture.allOf(hotCityListFuture, cityPyMapFuture);

        //3.查询全城市map
        try{
            completableFuture.get();
        }catch (Exception e){
            log.error("异步并发调用出现异常",e);
        }
        //4.构造返回
        List<CityDescVO>  hotCityList= hotCityListFuture.join();
        Map<String,List<CityDescVO>> cityPyMap = cityPyMapFuture.join();
        //5.构造对象并且返回
        CityPageVO cityPageVO = new CityPageVO();
        cityPageVO.setHotCityList(hotCityList);
        cityPageVO.setAllCityMap(cityPyMap);
        return cityPageVO;
    }


    /**
     * 获取热门城市
     * @return
     */
    private List<CityDescVO> getHotCityList() {

        List<CityDescVO> result = new ArrayList<>();
        R<List<RegionVO>> r = mapFeignClient.getHotCityList();

        if(null == r || r.getData() == null || r.getCode() != ResultCode.SUCCESS.getCode()){

            log.error("获得热门城市列表失败");
            return null;

        }
         result = BeanCopyUtil.copyList(r.getData(), CityDescVO::new);
        return result;
    }

    /**
     * 获取A-Z城市列表
     * @return
     */
    private Map<String, List<CityDescVO>> getCityPyMap() {

        Map<String, List<CityDescVO>> result = new HashMap<>();
        R<Map<String, List<RegionVO>>> r = mapFeignClient.getCityPinyinList();

        if(null == r || r.getData() == null || r.getCode() != ResultCode.SUCCESS.getCode()){

            log.error("获得A-Z城市列表失败");
            return null;
        }else{
            for(Map.Entry<String, List<RegionVO>> entry : r.getData().entrySet()){
                List<CityDescVO> cityDescVO = new ArrayList<>();
                result.put(entry.getKey(), BeanCopyUtil.copyList(entry.getValue(),CityDescVO::new));
            }
        }
        return result;
    }
}
