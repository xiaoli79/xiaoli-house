package org.xiaoli.xiaoliadminservice.house.domain.dto;


import lombok.Data;

@Data
public class HouseDescDTO {

    private Long houseId;
    private Long userId;
    private String title;
    private String rentType;
    private Double price;
    private String cityName;
    private String regionName;
    private String communityName;
    private String detailAddress;
    private String status;
    private String rentTimeCode;


}
