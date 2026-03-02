package org.xiaoli.xiaoliadminservice.map.service.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.xiaoli.xiaoliadminapi.map.constants.MapConstants;
import org.xiaoli.xiaoliadminservice.map.domain.dto.GeoResultDTO;
import org.xiaoli.xiaoliadminservice.map.domain.dto.LocationDTO;
import org.xiaoli.xiaoliadminservice.map.domain.dto.PoiListDTO;
import org.xiaoli.xiaoliadminservice.map.domain.dto.SuggestSearchDTO;
import org.xiaoli.xiaoliadminservice.map.service.IMapProvider;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;


@RefreshScope
@Component
@Data
@Slf4j
@ConditionalOnProperty(value = "map.type",havingValue = "qqmap")
public class QQMapServiceImpl implements IMapProvider {


    /**
     * 腾讯位置服务域名
     */
    @Value("${qqmap.apiServer}")
    private String apiServer;


    /**
     * 调用腾讯位置服务的密钥
     */
    @Value("${qqmap.key}")
    private String key;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public PoiListDTO searchQQMapPlaceByRegion(SuggestSearchDTO suggestSearchDTO) {
        // 1 构建请求url
        String url = String.format(
                apiServer + MapConstants.QQMAP_API_PLACE_SUGGESTION +
                        "?key=%s&region=%s&region_fix=1&page_index=%s&page_size=%s&keyword=%s",
                key, suggestSearchDTO.getId(), suggestSearchDTO.getPageIndex(), suggestSearchDTO.getPageSize(),suggestSearchDTO.getKeyword()
        );
        // 2 直接发送请求，并拿到返回结果再做对象转换
        ResponseEntity<PoiListDTO> response =  restTemplate.getForEntity(url, PoiListDTO.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("获取关键词查询结果异常", response);
            throw new ServiceException(ResultCode.QQMAP_QUERY_FAILED);
        }
        return response.getBody();


    }

    @Override
    public GeoResultDTO getQQMapDistrictByLonLat(LocationDTO locationDTO) {
        // 1 构建请求url
        String url = String.format(apiServer + MapConstants.QQMAP_GEOCODER +
                        "?key=%s&location=%s",
                key, locationDTO.formatInfo()
        );
        // 2 直接发送请求，并拿到返回结果再做对象转换
        ResponseEntity<GeoResultDTO> response =  restTemplate.getForEntity(url, GeoResultDTO.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("根据经纬度来获取区域信息查询结果异常", response);
            throw new ServiceException(ResultCode.QQMAP_QUERY_FAILED);
        }
        return response.getBody();
    }

}
