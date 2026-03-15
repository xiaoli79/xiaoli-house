package org.xiaoli.xiaoliportalservice.homepage.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoli.xiaoliadminapi.house.domain.dto.SearchHouseListReqDTO;
import org.xiaoli.xiaoliadminapi.house.domain.vo.HouseDetailVO;
import org.xiaoli.xiaoliadminapi.house.feign.HouseFeignClient;
import org.xiaoli.xiaoliadminapi.map.domain.dto.LocationReqDTO;
import org.xiaoli.xiaoliadminapi.map.domain.vo.RegionCityVO;
import org.xiaoli.xiaoliadminapi.map.feign.MapFeignClient;
import org.xiaoli.xiaolicommoncore.utils.BeanCopyUtil;
import org.xiaoli.xiaolicommoncore.utils.JsonUtil;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaolicommondomain.domain.vo.BasePageVO;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;
import org.xiaoli.xiaoliportalservice.homepage.domain.dto.CityDescDTO;
import org.xiaoli.xiaoliportalservice.homepage.domain.dto.DicDataDTO;
import org.xiaoli.xiaoliportalservice.homepage.domain.dto.HouseListReqDTO;
import org.xiaoli.xiaoliportalservice.homepage.domain.dto.PullDataListReqDTO;
import org.xiaoli.xiaoliportalservice.homepage.domain.vo.CityDescVO;
import org.xiaoli.xiaoliportalservice.homepage.domain.vo.DictsVO;
import org.xiaoli.xiaoliportalservice.homepage.domain.vo.HouseDescVO;
import org.xiaoli.xiaoliportalservice.homepage.domain.vo.PullDataListVO;
import org.xiaoli.xiaoliportalservice.homepage.service.IDictionaryService;
import org.xiaoli.xiaoliportalservice.homepage.service.IHomePageService;
import org.xiaoli.xiaoliportalservice.homepage.service.IRegionService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service


@Slf4j
public class HomePageServiceImpl implements IHomePageService {

    @Autowired
    private MapFeignClient mapFeignClient;

    @Autowired
    private IRegionService regionService;

    @Autowired
    private IDictionaryService dictionaryService;


    @Autowired
    private HouseFeignClient houseFeignClient;


    /**
     * 根据经纬度来查询城市信息
     * @param lat
     * @param lng
     * @return
     */
    @Override
    public CityDescVO getCityDesc(Double lat, Double lng) {
        if(null==lat||null==lng){
            throw new ServiceException("经纬度有一个或两个为空");
        }

        LocationReqDTO locationReqDTO = new LocationReqDTO();
        locationReqDTO.setLat(lat);
        locationReqDTO.setLng(lng);
//      设置其值
        R<RegionCityVO> result = mapFeignClient.locateCityByLocation(locationReqDTO);

        if(null == result|| result.getCode() != ResultCode.SUCCESS.getCode() || null == result.getData()) {
            throw new ServiceException("根据定位获取城市信息失败!!!");
        }

        CityDescVO cityDescVO = new CityDescVO();
        BeanUtils.copyProperties(result.getData(),cityDescVO);
        return cityDescVO;

    }


    /**
     * 获取下拉筛选数据列表
     * @param pullDataListReqDTO
     * @return
     */
    @Override
    public PullDataListVO getPullData(PullDataListReqDTO pullDataListReqDTO) {

        PullDataListVO result = new PullDataListVO();

        //1.查询城市下区域列表
        List<CityDescDTO> cityDescDTOS = regionService.regionChildren(pullDataListReqDTO.getCityId());
        result.setRegionList(BeanCopyUtil.copyList(cityDescDTOS,CityDescVO::new));

        //2.查询字典数据列表
        Map<String, List<DicDataDTO>> dicDataMap = dictionaryService.batchFindDictionaryDataByType(pullDataListReqDTO.getDirtTypes());

        Map<String,List<DictsVO>> dictMap = new HashMap<>();

        for(Map.Entry<String, List<DicDataDTO>> entry : dicDataMap.entrySet()){
            List<DictsVO> list = entry.getValue().stream()
                    .map(dictionaryDataDTO ->  {
                        DictsVO dictsVO = new DictsVO();
                        dictsVO.setId(dictionaryDataDTO.getId());
                        dictsVO.setKey(dictionaryDataDTO.getDataKey());
                        dictsVO.setName(dictionaryDataDTO.getValue());
                        return dictsVO;
                    }).collect(Collectors.toList());
            dictMap.put(entry.getKey(),list);
        }
        result.setDictMap(dictMap);


        //3.构造返回
        return result;
    }


    /**
     * 查询房源列表
     * @param houseListReqDTO
     * @return
     */
    @Override
    public BasePageVO<HouseDescVO> houseList(HouseListReqDTO houseListReqDTO) {




        //1.feign接口查询房源列表
        SearchHouseListReqDTO searchHouseListReqDTO = new SearchHouseListReqDTO();
        BeanUtils.copyProperties(houseListReqDTO,searchHouseListReqDTO);
        R<BasePageVO<HouseDetailVO>> r = houseFeignClient.searchList(searchHouseListReqDTO);
        if(null == r || r.getCode() != ResultCode.SUCCESS.getCode() || null == r.getData()) {
            log.error("查询房源列表失败:{}", JsonUtil.obj2String(searchHouseListReqDTO));
            throw new ServiceException("查询房源列表是啊表");
        }

        //构造返回：查询字典
        BasePageVO<HouseDescVO> result = new BasePageVO<>();
        result.setTotals(r.getData().getTotals());
        result.setTotalPages(r.getData().getTotalPages());
        result.setList(convertHouseList(r.getData().getList()));
        return result;
    }



    private List<HouseDescVO> convertHouseList(List<HouseDetailVO> list) {

        if(CollectionUtils.isEmpty(list)) {
            return null;
        }

        //1.查字典
        List<String> dataKeys = list.stream()
                .flatMap(houseDetailVO -> Stream.of(houseDetailVO.getRentType(),houseDetailVO.getPosition()))
                .distinct()
                .collect(Collectors.toList());


        Map<String,DicDataDTO> dicDataDTOMap = dictionaryService.batchFindDictionaryData(dataKeys);



        return list.stream()
                .map(houseDetailVO -> {
                    HouseDescVO houseDescVO = new HouseDescVO();
                    houseDescVO.setHouseId(houseDetailVO.getHouseId());
                    houseDescVO.setHeadImage(houseDetailVO.getHeadImage());
                    houseDescVO.setTitle(houseDetailVO.getTitle());
                    houseDescVO.setArea(houseDetailVO.getArea());
                    houseDescVO.setRegionName(houseDetailVO.getRegionName());
                    houseDescVO.setPrice(houseDetailVO.getPrice());


                    DicDataDTO rentTypeDTO = dicDataDTOMap.get(houseDetailVO.getRentType());
                    houseDescVO.setRentType(rentTypeDTO.getValue());

                    DicDataDTO positionData = dicDataDTOMap.get(houseDetailVO.getPosition());
                    houseDescVO.setPosition(positionData.getValue());
                    return houseDescVO;
                }).collect(Collectors.toList());

    }
}
