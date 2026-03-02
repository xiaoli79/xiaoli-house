package org.xiaoli.xiaoliadminservice.map.domain.dto;

import lombok.Data;

@Data
public class SysRegionDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 简称
     */
    private String name;


    /**
     * 区域全称
     */
    private String fullName;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 城市拼音
     */
    private String pinyin;

    /**
     * 城市级别
     */
    private Integer level;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;


}
