package org.xiaoli.xiaoliadminservice.house.domain.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HouseStatusEnum {


    UP("上架中"),
    DOWN("已下架"),
    RENTING("出租中");

    /**
     * 描述
     */
    private String msg;


    public static HouseStatusEnum getEnumByName(String name){
        for(HouseStatusEnum houseStatusEnum : HouseStatusEnum.values()){
            if(houseStatusEnum.name().equalsIgnoreCase(name)){
                return houseStatusEnum;
            }
        }
        return null;
    }

}
