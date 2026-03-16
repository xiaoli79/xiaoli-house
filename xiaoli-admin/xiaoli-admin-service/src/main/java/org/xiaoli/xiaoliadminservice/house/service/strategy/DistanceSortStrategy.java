package org.xiaoli.xiaoliadminservice.house.service.strategy;

import org.xiaoli.xiaoliadminapi.house.domain.dto.SearchHouseListReqDTO;
import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseDTO;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 距离排序策略
 */
public class DistanceSortStrategy implements ISortStrategy {


    //使用单例模式
    private static final DistanceSortStrategy INSATANCE= new DistanceSortStrategy();
    private DistanceSortStrategy() {}

    public static DistanceSortStrategy getInstance() {
        return INSATANCE;
    }

    @Override
    public List<HouseDTO> sort(List<HouseDTO> houseDTOList, SearchHouseListReqDTO reqDTO) {
        return houseDTOList.stream()
                .sorted(Comparator.comparingDouble(
                        houseDTO-> houseDTO.calculateDistance(reqDTO.getLongitude(),reqDTO.getLatitude())
                        )).collect(Collectors.toList());
    }
}
