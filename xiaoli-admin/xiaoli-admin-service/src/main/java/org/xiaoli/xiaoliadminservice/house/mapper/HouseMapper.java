package org.xiaoli.xiaoliadminservice.house.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseDescDTO;
import org.xiaoli.xiaoliadminservice.house.domain.dto.HouseListReqDTO;
import org.xiaoli.xiaoliadminservice.house.domain.entity.House;

import java.util.List;


@Mapper
public interface HouseMapper extends BaseMapper<House>{


    Long selectCountWithStatus(HouseListReqDTO houseListReqDTO);

    List<HouseDescDTO> selectPageWithStatus(HouseListReqDTO houseListReqDTO);



}

