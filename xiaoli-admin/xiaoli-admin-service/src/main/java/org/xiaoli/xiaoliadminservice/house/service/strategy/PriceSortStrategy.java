package org.xiaoli.xiaoliadminservice.house.service.strategy;

import org.xiaoli.xiaoliadminapi.house.domain.dto.SearchHouseListReqDTO;
import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseDTO;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 根据价格进行排序
 */
public class PriceSortStrategy implements ISortStrategy {


    //使用单例模式
    private final boolean asc;

    private final static PriceSortStrategy ASC_INSTANCE = new PriceSortStrategy(true);
    private final static PriceSortStrategy DESC_INSTANCE = new PriceSortStrategy(false);
    private PriceSortStrategy(boolean asc) {
        this.asc = asc;
    }

    public static PriceSortStrategy getInstance(boolean asc) {
        return asc ? ASC_INSTANCE : DESC_INSTANCE;
    }


    @Override
    public List<HouseDTO> sort(List<HouseDTO> houseDTOList, SearchHouseListReqDTO reqDTO) {
        if(asc){
            return houseDTOList.stream()
                    .sorted(Comparator.comparingDouble(HouseDTO::getPrice))
                    .collect(Collectors.toList());
        }else{
            return houseDTOList.stream()
                    .sorted(Comparator.comparingDouble(HouseDTO::getPrice).reversed())
                    .collect(Collectors.toList());




        }
    }
}
