package org.xiaoli.xiaoliadminservice.map.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoli.xiaoliadminapi.map.constants.MapConstants;
import org.xiaoli.xiaoliadminapi.map.domain.dto.LocationReqDTO;
import org.xiaoli.xiaoliadminapi.map.domain.dto.PlaceSearchReqDTO;
import org.xiaoli.xiaoliadminservice.config.service.ISysArgumentService;
import org.xiaoli.xiaoliadminservice.map.domain.dto.*;
import org.xiaoli.xiaoliadminservice.map.domain.entity.SysRegion;
import org.xiaoli.xiaoliadminservice.map.mapper.RegionMapper;
import org.xiaoli.xiaoliadminservice.map.service.IMapProvider;
import org.xiaoli.xiaoliadminservice.map.service.IXiaoliMapService;
import org.xiaoli.xiaolicommoncache.utils.CacheUtil;
import org.xiaoli.xiaolicommoncore.domain.dto.BasePageDTO;
import org.xiaoli.xiaolicommoncore.utils.PageUtil;
import org.xiaoli.xiaolicommonredis.service.RedisService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class XiaoliMapServiceImpl implements IXiaoliMapService {

    /**
     * sys_region的mapper
     */
    @Autowired
    private RegionMapper regionMapper;

    /**
     * redis服务对象
     */
    @Autowired
    private RedisService redisService;

    /**
     * 本地内存服务对象~~
     */
    @Autowired
    private Cache<String,Object> caffeineCache;


    /**
     * 腾讯位置服务的服务类
     */
    @Autowired
    private IMapProvider mapProvider;


    @Autowired
    private ISysArgumentService sysArgumentService;



    @PostConstruct
    public void initCityMap(){
//      查询数据库!!
        List<SysRegion> list = regionMapper.selectAllRegion();
//      在服务启动期间,缓存城市列表~~
        loadCityInfo(list);
//      在服务启动期间,缓存拼音归类的城市列表~~
        loadCityPinyinInfo(list);
    }

    private void loadCityPinyinInfo(List<SysRegion> list) {

//      这是来记录其值!!
        Map<String,List<SysRegionDTO>> result = new LinkedHashMap<>();

        for(SysRegion sysRegion : list){
            if(sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)){
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion,sysRegionDTO);
                String cityPinyin = sysRegion.getPinyin().toUpperCase().substring(0,1);

                if(result.containsKey(cityPinyin)) {
                    result.get(cityPinyin).add(sysRegionDTO);
                }else{
//                 先对列表进行初始化
                    List<SysRegionDTO> regionDtoList = new ArrayList<>();
//                 然后添加一下~~
                    regionDtoList.add(sysRegionDTO);
//                 放入其值
                    result.put(cityPinyin,regionDtoList);
                }
            }
        }
//      构建缓存
        CacheUtil.setL2Cache(redisService,MapConstants.CACHE_MAP_CITY_PINYIN_KEY,result,caffeineCache,120L,TimeUnit.MINUTES);
    }

    /**
     * 直接通过mysql来进行查询
     * @param list
     */
    private void loadCityInfo(List<SysRegion> list) {
        List<SysRegionDTO> result = new ArrayList<>();

//      SysRegion这个类转换成sysRegion~~
        for (SysRegion sysRegion : list) {
            if(sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)){
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion,sysRegionDTO);
                result.add(sysRegionDTO);
            }
        }
//      设置缓存
        CacheUtil.setL2Cache(redisService,MapConstants.CACHE_MAP_CITY_KEY,result,caffeineCache,120L,TimeUnit.MINUTES);
    }

    /**
     * 城市列表查询 V1
     * @return 城市列表信息
     */
    public List<SysRegionDTO> getCityListV1() {
        // 1 声明一个空列表
        List<SysRegionDTO> result = new ArrayList<>();
        // 2 查询数据库
        List<SysRegion> list =  regionMapper.selectAllRegion();
        // 3 提取城市数据列表,并且做对象转换
        for (SysRegion sysRegion : list) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)) {
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                result.add(sysRegionDTO);
            }
        }
        return result;
    }


    /**
     * 通过mysql和redis来进行查询地图列表信息
     * @return
     */
    public List<SysRegionDTO> getCityListV2() {
//       声明一个空列表
        List<SysRegionDTO> result = new ArrayList<>();
//       查询数据库
        List<SysRegionDTO> cache = redisService.getCacheObject(MapConstants.CACHE_MAP_CITY_KEY, new TypeReference<List<SysRegionDTO>>() {
        });

        if(cache != null){
            return cache;
        }

        List<SysRegion> list = regionMapper.selectAllRegion();
//       提取城市数据列表，并且做对象转换
        for(SysRegion sysRegion : list){
            if(sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)){
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion,sysRegionDTO);
                result.add(sysRegionDTO);
            }
        }
//      设置缓存
        redisService.setCacheObject(MapConstants.CACHE_MAP_CITY_KEY,result);
        return result;
    }

    /**
     * 通过mysql和redis以及二级缓存来进行相应的查询操作！！
     * @return
     */
    public List<SysRegionDTO> getCityListV3() {

//       声明一个空列表
        List<SysRegionDTO> result = new ArrayList<>();
//      获取二级缓存，也就是本地缓存
        List<SysRegionDTO> cache = CacheUtil.getL2Cache(redisService,MapConstants.CACHE_MAP_CITY_KEY,new TypeReference<List<SysRegionDTO>>() {},caffeineCache);
        if(cache != null){
            return cache;
        }

//      从数据库中查询数据
        List<SysRegion> list = regionMapper.selectAllRegion();
//       提取城市数据列表，并且做对象转换
        for(SysRegion sysRegion : list){
            if(sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)){
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion,sysRegionDTO);
                result.add(sysRegionDTO);
            }
        }

//      设置缓存
        CacheUtil.setL2Cache(redisService,MapConstants.CACHE_MAP_CITY_KEY,result,caffeineCache,120L, TimeUnit.MINUTES);
        return result;

    }


    /**
     * 缓存预热方案~~避免刚开始请求量过多直接把数据库给压爆~~
     * @return
     */
    @Override
    public List<SysRegionDTO> getCityList() {
        List<SysRegionDTO> cache = CacheUtil.getL2Cache(redisService,MapConstants.CACHE_MAP_CITY_KEY,new TypeReference<List<SysRegionDTO>>() {},caffeineCache);
        return cache;
    }

    @Override
    public Map<String, List<SysRegionDTO>> getCityPinyinList() {
        Map<String, List<SysRegionDTO>> cache = CacheUtil.getL2Cache(redisService,MapConstants.CACHE_MAP_CITY_PINYIN_KEY,new TypeReference<Map<String,List<SysRegionDTO>>>() {},caffeineCache);
        return cache;
    }


    /**
     * 根据父级ID，查询子级相关的信息
     * @param parentId
     * @return
     */
    @Override
    public List<SysRegionDTO> getRegionChildren(Long parentId) {

        String key = MapConstants.CACHE_MAP_CITY_CHILDREN_KEY + parentId;
//      查询缓存
        List<SysRegionDTO> l2Cache = CacheUtil.getL2Cache(redisService, key, new TypeReference<List<SysRegionDTO>>() {
        }, caffeineCache);
        if(l2Cache != null){
            return l2Cache;
        }

        List<SysRegionDTO> result = new ArrayList<>();
//      查询数据库
        List<SysRegion> sysRegions = regionMapper.selectAllRegion();

//      并把相应的数据转换为DTO
        for (SysRegion sysRegion : sysRegions) {
//          这样是避免空指针异常的方式~~
            if(parentId.equals(sysRegion.getParentId())){
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion,sysRegionDTO);
                result.add(sysRegionDTO);
            }
        }

//      设置缓存
        CacheUtil.setL2Cache(redisService,key,result,caffeineCache,120L, TimeUnit.MINUTES);
//      返回其值！！
        return result;
    }


    /**
     * 获取热门城市列表
     * @return 城市列表
     */
    @Override
    public List<SysRegionDTO> getHotCityList() {


//      查询还二级缓存
        List<SysRegionDTO> l2Cache = CacheUtil.getL2Cache(redisService, MapConstants.CACHE_MAP_HOT_CITY, new TypeReference<List<SysRegionDTO>>() {
        }, caffeineCache);
        if(l2Cache != null){
            return l2Cache;
        }
//      这是热门城市的ID
        String ids = sysArgumentService.getByConfigKey(MapConstants.CONFIG_KEY).getValue();

        List<Long> idList = new ArrayList<>();
        for(String id : ids.split(",")){
            idList.add(Long.valueOf(id));
        }
        List<SysRegionDTO> result = new ArrayList<>();

//      regionMapper.selectBatchIds(idList)这个方法是根据ID批量查询城市~~
        for(SysRegion sysRegion: regionMapper.selectBatchIds(idList)){
            SysRegionDTO sysRegionDTO = new SysRegionDTO();
            BeanUtils.copyProperties(sysRegion,sysRegionDTO);
            result.add(sysRegionDTO);
        }

//      设置缓存
        CacheUtil.setL2Cache(redisService,MapConstants.CACHE_MAP_HOT_CITY,result,caffeineCache,120L, TimeUnit.MINUTES);


        return result;
    }

    /**
     * 根据地点搜索
     * @param placeSearchReqDTO 搜索条件
     * @return 搜索结果
     */
    @Override
    public BasePageDTO<SearchPoiDTO> searchSuggestOnMap(PlaceSearchReqDTO placeSearchReqDTO) {

//      构造查询腾讯位置服务的入参
        SuggestSearchDTO suggestSearchDTO = new SuggestSearchDTO();
        BeanUtils.copyProperties(placeSearchReqDTO,suggestSearchDTO);
        suggestSearchDTO.setPageIndex(placeSearchReqDTO.getPageNo());
        suggestSearchDTO.setId(String.valueOf(placeSearchReqDTO.getId()));

//      调用相关的方法
        PoiListDTO poiListDTO = mapProvider.searchQQMapPlaceByRegion(suggestSearchDTO);

//      进行结果对象转换
        List<PoiDTO> poiDTOList = poiListDTO.getData();
        BasePageDTO<SearchPoiDTO> result = new BasePageDTO<>();
        result.setTotals(poiListDTO.getCount());
        result.setTotalPages(PageUtil.getTotalPages(result.getTotals(),placeSearchReqDTO.getPageSize()));
        List<SearchPoiDTO> pageRes = new ArrayList<>();
        for(PoiDTO poiDTO : poiDTOList){
            SearchPoiDTO searchPoiDTO = new SearchPoiDTO();
            BeanUtils.copyProperties(poiDTO,searchPoiDTO);
            searchPoiDTO.setLongitude(poiDTO.getLocation().getLng());
            searchPoiDTO.setLatitude(poiDTO.getLocation().getLat());
            pageRes.add(searchPoiDTO);
        }

        result.setList(pageRes);
        return result;
    }
    /**
     * 根据经纬度来定位城市
     * @param locationReqDTO
     * @return
     */
    @Override
    public RegionCityDTO getCityByLocation(LocationReqDTO locationReqDTO) {


//      构建查询腾讯服务的入参
        LocationDTO locationDTO = new LocationDTO();
        BeanUtils.copyProperties(locationReqDTO,locationDTO);

        RegionCityDTO result = new RegionCityDTO();

//      调用相关的方法
        GeoResultDTO geoResultDTO = mapProvider.getQQMapDistrictByLonLat(locationDTO);

//      判断是否为空
        if(geoResultDTO != null && geoResultDTO.getResult() != null && geoResultDTO.getResult().getAd_info()!= null){

            String cityName =  geoResultDTO.getResult().getAd_info().getCity();

//          查询缓存
            List<SysRegionDTO> cache = CacheUtil.getL2Cache(redisService,MapConstants.CACHE_MAP_CITY_KEY,new TypeReference<List<SysRegionDTO>>() {},caffeineCache);

            for(SysRegionDTO sysRegionDTO : cache){
                if(sysRegionDTO.getFullName().equals(cityName)){
                    BeanUtils.copyProperties(sysRegionDTO,result);
                    return result;
                }
            }
        }
        return result;
    }
}
