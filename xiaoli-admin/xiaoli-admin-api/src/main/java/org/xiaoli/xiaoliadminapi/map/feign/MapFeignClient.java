package org.xiaoli.xiaoliadminapi.map.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.xiaoli.xiaoliadminapi.map.domain.dto.LocationReqDTO;
import org.xiaoli.xiaoliadminapi.map.domain.dto.PlaceSearchReqDTO;
import org.xiaoli.xiaoliadminapi.map.domain.vo.RegionCityVO;
import org.xiaoli.xiaoliadminapi.map.domain.vo.RegionVO;
import org.xiaoli.xiaoliadminapi.map.domain.vo.SearchPoiVO;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.vo.BasePageVO;

import java.util.List;
import java.util.Map;


/**
 * 区域信息VO
 */
@FeignClient(contextId = "mapFeignClient",value = "xiao-admin")
public interface MapFeignClient {

    /**
     * 城市列表查询
     * @return 城市列表信息
     */
    @GetMapping("/map/city_list")
    R<List<RegionVO>> getCityList();


    /**
     * 城市拼音归类查询
     * @return
     */

    @GetMapping("/map/city_pinyin_list")
    R<Map<String,List<RegionVO>>> getCityPinyinList();


    /**
     * 根据父级ID查询子级相关的信息
     * @param parentId
     * @return
     */
    @GetMapping("/map/region_children_list")
    R<List<RegionVO>> regionChildren(@RequestParam Long parentId);


    /**
     * 获取城市列表
     */
    @GetMapping("/map/city_hot_list")
    R<List<RegionVO>> getHotCityList();


    /**
     * 根据地点搜索
     * @param placeSearchReqDTO 搜索条件
     * @return 搜索结果
     */
    @PostMapping("/map/search")
    R<BasePageVO<SearchPoiVO>> searchSuggestOnMap(@RequestBody PlaceSearchReqDTO placeSearchReqDTO);


    /**
     * 根据经纬度来定位城市
     * @param locationReqDTO
     * @return
     */
    @PostMapping("/map/locate_city_by_location")
    R<RegionCityVO> locateCityByLocation(@RequestBody LocationReqDTO locationReqDTO);

}
