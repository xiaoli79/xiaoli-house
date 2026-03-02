package org.xiaoli.xiaoliadminservice.map.controller;

import jodd.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoli.xiaoliadminapi.map.domain.dto.LocationReqDTO;
import org.xiaoli.xiaoliadminapi.map.domain.dto.PlaceSearchReqDTO;
import org.xiaoli.xiaoliadminapi.map.domain.vo.RegionCityVO;
import org.xiaoli.xiaoliadminapi.map.domain.vo.RegionVO;
import org.xiaoli.xiaoliadminapi.map.domain.vo.SearchPoiVO;
import org.xiaoli.xiaoliadminapi.map.feign.MapFeignClient;
import org.xiaoli.xiaoliadminservice.map.domain.dto.RegionCityDTO;
import org.xiaoli.xiaoliadminservice.map.domain.dto.SearchPoiDTO;
import org.xiaoli.xiaoliadminservice.map.domain.dto.SysRegionDTO;
import org.xiaoli.xiaoliadminservice.map.service.IXiaoliMapService;
import org.xiaoli.xiaolicommoncore.domain.dto.BasePageDTO;
import org.xiaoli.xiaolicommoncore.utils.BeanCopyUtil;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.vo.BasePageVO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class XiaoliMapController implements MapFeignClient {


    @Autowired
    private IXiaoliMapService xiaoliMapService;
    /**
     * 城市列表查询
     * @return 城市列表信息
     */

    @Override
    public R<List<RegionVO>> getCityList() {
        List<SysRegionDTO> cityList = xiaoliMapService.getCityList();
        List<RegionVO> result = BeanCopyUtil.copyList(cityList, RegionVO::new);
        return R.ok(result);
    }


    /**
     * 根据城市拼音归类查询x
     * @return
     */
    @Override
    public R<Map<String, List<RegionVO>>> getCityPinyinList() {
        Map<String,List<RegionVO>> result = new LinkedHashMap<>();
        Map<String,List<SysRegionDTO>> pinyinList =  xiaoliMapService.getCityPinyinList();
        for(Map.Entry<String,List<SysRegionDTO>> entry:pinyinList.entrySet()){
            result.put(entry.getKey(),BeanCopyUtil.copyList(entry.getValue(),RegionVO::new));
        }
        return R.ok(result);
    }

    /**
     * 根据父级ID查询子级相关的信息
     * @param parentId
     * @return
     */
    @Override
    public R<List<RegionVO>> regionChildren(@RequestParam Long parentId) {

        List<SysRegionDTO> list =  xiaoliMapService.getRegionChildren(parentId);
//      进行对象的转换，将DTO转换为VO
        List<RegionVO> result = BeanCopyUtil.copyList(list, RegionVO::new);
        return R.ok(result);

    }
    /**
     * 获取热门城市列表
     * @return 城市列表
     */
    @Override
    public R<List<RegionVO>> getHotCityList() {
        List<SysRegionDTO> hotCityList = xiaoliMapService.getHotCityList();
//      这是把SysRegionDTO转变为RegionVO~~
        List<RegionVO> result = BeanCopyUtil.copyList(hotCityList, RegionVO::new);
        return R.ok(result);
    }



    /**
     * 根据地点搜索
     * @param placeSearchReqDTO 搜索条件
     * @return 搜索结果
     */
    @Override
    public R<BasePageVO<SearchPoiVO>> searchSuggestOnMap(@RequestBody PlaceSearchReqDTO placeSearchReqDTO) {
        BasePageDTO<SearchPoiDTO> searchPoiDto =  xiaoliMapService.searchSuggestOnMap(placeSearchReqDTO);
        BasePageVO<SearchPoiVO> result = new BasePageVO<>();
        BeanUtils.copyProperties(searchPoiDto,result);
        return R.ok(result);
    }

    /**
     * 根据经纬度来定位城市
     * @param locationReqDTO
     * @return
     */
    @Override
    public R<RegionCityVO> locateCityByLocation(@RequestBody LocationReqDTO locationReqDTO) {
       RegionCityDTO regionCityDTO = xiaoliMapService.getCityByLocation(locationReqDTO);

       RegionCityVO regionCityVO = new RegionCityVO();
       BeanUtils.copyProperties(regionCityDTO,regionCityVO);
       return R.ok(regionCityVO);



    }
}
