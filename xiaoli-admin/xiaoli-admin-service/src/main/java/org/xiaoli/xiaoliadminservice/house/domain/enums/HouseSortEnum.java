package org.xiaoli.xiaoliadminservice.house.domain.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HouseSortEnum {


    DISTANCE("距离优先"),
    PRICE_DESC("价格从高到低"),
    PRICE_ASC("价格从低到高");

    private String desc;


}
