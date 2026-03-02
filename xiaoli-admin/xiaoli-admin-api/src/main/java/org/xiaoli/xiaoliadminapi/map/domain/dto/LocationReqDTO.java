package org.xiaoli.xiaoliadminapi.map.domain.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 位置查询的DTO
 */

@Data
public class LocationReqDTO {

    @NotNull(message = "纬度不能为空")
    private Double lat;


    @NotNull(message = "经度不能为空")
    private Double lng;


    /**
     * 格式化后的经纬度
     * @return
     */
    public String formatInfo(){
        return lat+","+lng;
    }



}
