package org.xiaoli.xiaoliadminservice.config.service;


import org.xiaoli.xiaoliadminapi.config.domain.dto.*;
import org.xiaoli.xiaoliadminapi.config.domain.vo.DictionaryDataVo;
import org.xiaoli.xiaoliadminapi.config.domain.vo.DictionaryTypeVO;
import org.xiaoli.xiaolicommondomain.domain.vo.BasePageVO;

import java.util.List;
import java.util.Map;

/**
 * 字典服务的接口
 */

public interface ISysDictionaryService {


    /**
     * 新增字典类型
     * @param reqDTO 新增字典类型DTO
     * @return Long
     */
    Long addType(DictionaryTypeWriteReqDTO reqDTO);


    /**
     * 字典类型累表
     * @param dictionaryTypeListReqDTO 入参
     * @return BasePageVO
     */
    BasePageVO<DictionaryTypeVO> listType(DictionaryTypeListReqDTO dictionaryTypeListReqDTO);


    /**
     * 编辑字典类型
     * @param dictionaryTypeWriteReqDTO 编辑字典类型DTO
     * @return Long
     */
    Long editType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO);


    /**
     * 新增字典类型
     * @param dictionaryDataAddReqDTO 字典类型的DTO
     * @return
     */
    Long addData(DictionaryDataAddReqDTO dictionaryDataAddReqDTO);

    BasePageVO<DictionaryDataVo> listData(DictionaryDataListReqDTO dictionaryDataListReqDTO);


    Long editData(DictionaryDataEditDTO dictionaryDataEditDTO);


    /**
     * 通过字典类型来查询字典数据
     * @param typeKey
     * @return
     */
    List<DictionaryDataDTO> selecDataByType(String typeKey);

    Map<String, List<DictionaryDataDTO>> selecDataByTypes(List<String> typeKeys);

    DictionaryDataDTO getDicDataByKey(String dataKey);

    List<DictionaryDataDTO> getDicDataByKeys(List<String> dataKeys);
}
