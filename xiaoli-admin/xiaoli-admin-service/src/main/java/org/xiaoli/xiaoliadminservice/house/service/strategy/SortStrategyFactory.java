package org.xiaoli.xiaoliadminservice.house.service.strategy;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.xiaoli.xiaoliadminservice.house.domain.enums.HouseSortEnum;



@Slf4j
public class SortStrategyFactory {


    /**
     * 根据排序规则返回对应的排序策略
     * @param sort
     * @return
     */
    public static ISortStrategy getSortStrategy(String sort){

        if(StringUtils.isNotBlank(sort)){
            if(sort.equalsIgnoreCase(HouseSortEnum.DISTANCE.name())){
                return DistanceSortStrategy.getInstance();
            }else if(sort.equalsIgnoreCase(HouseSortEnum.PRICE_ASC.name())){
                return PriceSortStrategy.getInstance(true);
            }else if(sort.equalsIgnoreCase(HouseSortEnum.PRICE_DESC.name())){
                return PriceSortStrategy.getInstance(false);
            }else{
                log.error("不存在排序规则，将按照距离排序");
                return DistanceSortStrategy.getInstance();
            }
        }
        return DistanceSortStrategy.getInstance();
    }
}
