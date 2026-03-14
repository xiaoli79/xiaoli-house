package org.xiaoli.xiaoliportalservice.homepage.service;

import org.xiaoli.xiaoliportalservice.homepage.domain.dto.CityDescDTO;
import org.xiaoli.xiaoliportalservice.homepage.domain.vo.CityDescVO;

import java.util.List;

public interface IRegionService {


    /**
     * 根据父级ID来获取子集区域列表
     * @param parentId
     * @return
     */
    List<CityDescDTO> regionChildren(Long parentId);
}
