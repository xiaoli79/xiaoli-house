package org.xiaoli.xiaoliadminservice.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoli.xiaoliadminapi.config.domain.dto.*;
import org.xiaoli.xiaoliadminapi.config.domain.vo.DictionaryDataVo;
import org.xiaoli.xiaoliadminapi.config.domain.vo.DictionaryTypeVO;
import org.xiaoli.xiaoliadminapi.config.feign.DicitonaryFeignClient;
import org.xiaoli.xiaoliadminservice.config.service.ISysDictionaryService;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.vo.BasePageVO;

import java.util.List;
import java.util.Map;

/**
 * 字典服务的相关的接口
 */
@RestController
public class DictionaryController implements DicitonaryFeignClient {


    @Autowired
    private ISysDictionaryService sysDictionaryService;


    @PostMapping("/dictionary_type/add")
    public R<Long> addType(@RequestBody @Validated DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        return  R.ok(sysDictionaryService.addType(dictionaryTypeWriteReqDTO));
    }


    /**
     * 字典类型列表
     * @param dictionaryTypeListReqDTO 字典类型列表DTO
     * @return BasePageVO
     */
    @GetMapping("/dictionary_type/list")
    public R<BasePageVO<DictionaryTypeVO>> listType(@Validated DictionaryTypeListReqDTO dictionaryTypeListReqDTO) {
        return R.ok(sysDictionaryService.listType(dictionaryTypeListReqDTO));
    }

    /**
     * 编辑字典类型
     * @param dictionaryTypeWriteReqDTO 编辑字典类型值DTO
     * @return Long
     */
    @PostMapping("/dictionary_type/edit")
    public R<Long> editType(@RequestBody @Validated DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        return R.ok(sysDictionaryService.editType(dictionaryTypeWriteReqDTO));
    }


    /**
     * 新增字典数据
     * @param dictionaryDataAddReqDTO 新增字典数据的DTO
     * @return Long
     */
    @PostMapping("/dictionary_data/add")
    public R<Long> addData(@RequestBody @Validated DictionaryDataAddReqDTO dictionaryDataAddReqDTO){
        return R.ok(sysDictionaryService.addData(dictionaryDataAddReqDTO));
    }

    /**
     * 查询字典数据
     * @param dictionaryDataListReqDTO 查询字典数据的DTO
     * @return
     */
    @GetMapping("/dictionary_data/list")
    public R<BasePageVO<DictionaryDataVo>> listData(@Validated DictionaryDataListReqDTO dictionaryDataListReqDTO){
        return R.ok(sysDictionaryService.listData(dictionaryDataListReqDTO));


    }

    /**
     * 编辑字典数据
     * @param dictionaryDataEditDTO
     * @return
     */
    @PostMapping("/dictionary_data/edit")
    public R<Long> editData(@RequestBody @Validated DictionaryDataEditDTO dictionaryDataEditDTO){
        return R.ok(sysDictionaryService.editData(dictionaryDataEditDTO));
    }

    @Override
    public List<DictionaryDataDTO> selecDataByType(String typeKey) {
        return sysDictionaryService.selecDataByType(typeKey);
    }

    /**
     * 获取多个字典类型下的所有字典数据
     * @param typeKey  多个字典类型
     * @return 哈希，key对应的是字典类型的建，value对应的是字典类型下的所有字典数据
     */
    @Override
    public Map<String, List<DictionaryDataDTO>> selecDataByTypes(List<String> typeKey) {
        return sysDictionaryService.selecDataByTypes(typeKey);
    }


    /**
     * 根据字典数据业务主键获取字典数据对象
     * @param dataKey 字典数据业务主键
     * @return 字典数据对象
     */
    @Override
    public DictionaryDataDTO getDicDataByKey(String dataKey) {
       return sysDictionaryService.getDicDataByKey(dataKey);
    }

    @Override
    public List<DictionaryDataDTO> getDicDataByKeys(List<String> dataKeys) {
        return sysDictionaryService.getDicDataByKeys(dataKeys);
    }


}
