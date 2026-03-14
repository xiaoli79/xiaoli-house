package org.xiaoli.xiaoliportalservice.homepage.service;




import org.xiaoli.xiaoliportalservice.homepage.domain.dto.DicDataDTO;

import java.util.List;
import java.util.Map;

public interface IDictionaryService {


    /**
     * 根据字典数据类型来查询字典数据列表
     * @param types
     * @return
     */
    Map<String, List<DicDataDTO>> batchFindDictionaryDataByType(List<String> types);

}
