package org.xiaoli.xiaoliadminservice.map.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xiaoli.xiaoliadminservice.map.domain.entity.SysRegion;

import java.util.List;


@Mapper
public interface RegionMapper extends BaseMapper<SysRegion> {


    /**
     * 获取全量区域信息
     * @return 区域列表
     */
    List<SysRegion> selectAllRegion();

}
