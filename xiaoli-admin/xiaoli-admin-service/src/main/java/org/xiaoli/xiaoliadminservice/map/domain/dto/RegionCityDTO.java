package org.xiaoli.xiaoliadminservice.map.domain.dto;


import lombok.Data;


/**
 * 城市信息的DTO
 */
@Data
public class RegionCityDTO {

    /**
     * 城市ID
     */

    private Long id;

    /**
     * 城市名称
     */
    private String name;

    /**
     * 城市全称
     */
    private String fullName;



}
