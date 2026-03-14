package org.xiaoli.xiaoliportalservice.homepage.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoli.xiaoliadminapi.map.domain.vo.RegionVO;
import org.xiaoli.xiaoliadminapi.map.feign.MapFeignClient;
import org.xiaoli.xiaolicommoncore.utils.JsonUtil;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaolicommonredis.service.RedisService;
import org.xiaoli.xiaoliportalservice.homepage.domain.dto.CityDescDTO;
import org.xiaoli.xiaoliportalservice.homepage.service.IRegionService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@Slf4j
public class RegionServiceImpl implements IRegionService {



    private static final String REGION_CHILDREN_PREFIX = "applet:region:children";


    private static final Long REGION_CHILDREN_TIMEOUT = 24 * 60L;




    @Autowired
    private RedisService redisService;


    @Autowired
    private MapFeignClient mapFeignClient;


    /**
     * 根据父级ID来获取子集区域列表
     * @param parentId
     * @return
     */
    @Override
    public List<CityDescDTO> regionChildren(Long parentId) {


        //1.判断逻辑
        if(null == parentId){
            log.error("区域ID为空，无法查询子区域列表");
            return null;
        }

        //1.查缓存
        List<CityDescDTO> regionList =  getCacheRegionList(parentId);

        if(!CollectionUtils.isEmpty(regionList)){
            return regionList;
        }

        //2.不存在：feign、缓存
        R<List<RegionVO>> r = mapFeignClient.regionChildren(parentId);
        if(null == r || CollectionUtils.isEmpty(r.getData()) || r.getCode() != ResultCode.SUCCESS.getCode()){
            log.error("查询子区域列表失败");
            return null;
        }

        regionList = r.getData().stream()
                .map(regionVO -> {
                    CityDescDTO cityDescDTO = new CityDescDTO();
                    cityDescDTO.setId(regionVO.getId());
                    cityDescDTO.setName(regionVO.getName());
                    cityDescDTO.setFullName(regionVO.getFullName());
                    return cityDescDTO;

                }).collect(Collectors.toList());
        //3.存在:返回

        cacheRegionList(parentId,regionList);



        return regionList;
    }


    /**
     * 进行缓存
     * @param parentId
     */
    private void cacheRegionList(Long parentId,List<CityDescDTO> regionList) {

        if(null == parentId){
            return;
        }

        //1.设置缓存
        redisService.setCacheObject(
                REGION_CHILDREN_PREFIX+parentId,
                JsonUtil.obj2String(regionList),
                REGION_CHILDREN_TIMEOUT, TimeUnit.MINUTES);
    }


    /**
     * 查缓存
     * @param parentId
     * @return
     */
    private List<CityDescDTO> getCacheRegionList(Long parentId) {
        String str = redisService.getCacheObject(REGION_CHILDREN_PREFIX + parentId, String.class);

        //1.进行判空校验
        if(null == str){
            return null;
        }
        return JsonUtil.string2List(str, CityDescDTO.class);
    }


}
