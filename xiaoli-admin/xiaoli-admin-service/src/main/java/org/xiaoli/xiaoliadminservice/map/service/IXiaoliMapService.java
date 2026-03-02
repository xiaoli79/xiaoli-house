package org.xiaoli.xiaoliadminservice.map.service;


import org.xiaoli.xiaoliadminapi.map.domain.dto.LocationReqDTO;
import org.xiaoli.xiaoliadminapi.map.domain.dto.PlaceSearchReqDTO;
import org.xiaoli.xiaoliadminservice.map.domain.dto.RegionCityDTO;
import org.xiaoli.xiaoliadminservice.map.domain.dto.SearchPoiDTO;
import org.xiaoli.xiaoliadminservice.map.domain.dto.SysRegionDTO;
import org.xiaoli.xiaolicommoncore.domain.dto.BasePageDTO;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.vo.BasePageVO;

import java.util.List;
import java.util.Map;

public interface IXiaoliMapService {

    /**
     * 城市列表查询
     * @return 城市列表信息
     */
    List<SysRegionDTO> getCityList();

    /**
     * 城市拼音归类查询
     * @return 城市字母与城市列表的哈希
     */
    Map<String, List<SysRegionDTO>> getCityPinyinList();

    /**
     * 根据父级区域ID获取子集区域列表
     * @param parentId 父级区域ID
     * @return 子集区域列表
     */
    List<SysRegionDTO> getRegionChildren(Long parentId);
    /**
     * 获取热门城市列表
     * @return 城市列表
     */
    List<SysRegionDTO> getHotCityList();
    /**
     * 根据地点搜索
     * @param placeSearchReqDTO 搜索条件
     * @return 搜索结果
     */
    BasePageDTO<SearchPoiDTO> searchSuggestOnMap(PlaceSearchReqDTO placeSearchReqDTO);

    /**
     * 根据经纬度来定位城市
     * @param locationReqDTO
     * @return
     */
    RegionCityDTO getCityByLocation(LocationReqDTO locationReqDTO);
}



