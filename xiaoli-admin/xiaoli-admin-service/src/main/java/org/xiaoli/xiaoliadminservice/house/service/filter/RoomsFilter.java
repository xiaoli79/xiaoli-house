package org.xiaoli.xiaoliadminservice.house.service.filter;


import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import org.springframework.stereotype.Component;
import org.xiaoli.xiaoliadminapi.house.domain.dto.SearchHouseListReqDTO;
import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseDTO;


/**
 * 居室过滤
 */
@Component
public class RoomsFilter  implements IHouseFilter {


    @Override
    public Boolean filter(HouseDTO houseDTO, SearchHouseListReqDTO reqDTO) {
        return CollectionUtils.isEmpty(reqDTO.getRooms())  || reqDTO.getRooms().contains(houseDTO.getRooms());
    }

}
