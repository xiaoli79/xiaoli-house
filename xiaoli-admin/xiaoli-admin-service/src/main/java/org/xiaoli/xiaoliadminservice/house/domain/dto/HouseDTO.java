package org.xiaoli.xiaoliadminservice.house.domain.dto;


import lombok.Data;

import java.util.List;

/**
 * 完整的房源信息
 */
@Data
public class HouseDTO {

    private Long houseId;
    // 房东信息
    private Long userId;
    private String nickName;
    private String avatar;
    // 房屋基本信息
    private String title;
    private String rentType;
    private Integer floor;
    private Integer allFloor;
    private String houseType;
    private String rooms;
    private String position;
    private Double area;
    private Double price;
    private String intro;

    // 设备列表
    private List<DeviceDTO> devices;
    // 标签列表
    private List<TagDTO> tags;

    private String headImage;
    private List<String> images;

    // 位置信息
    private Long cityId;
    private String cityName;
    private Long regionId;
    private String regionName;
    private String communityName;
    private String detailAddress;
    private Double longitude;
    private Double latitude;

    // 状态信息
    private String status;
    private String rentTimeCode;



}
