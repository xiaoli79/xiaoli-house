package org.xiaoli.xiaoliadminservice.map.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 经纬度DTO
 */
@Data
public class LocationDTO {

    /**
     * 纬度
     */

    private Double lat;

    /**
     * 经度
     */

    private Double lng;


    /**
     * 格式化后的经纬度
     * @return
     */
    public String formatInfo(){
        return lat + "," + lng;
    }
}
