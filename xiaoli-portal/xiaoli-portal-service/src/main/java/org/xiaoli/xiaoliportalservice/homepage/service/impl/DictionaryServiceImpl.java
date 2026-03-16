package org.xiaoli.xiaoliportalservice.homepage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoli.xiaoliadminapi.config.domain.dto.DictionaryDataDTO;
import org.xiaoli.xiaoliadminapi.config.feign.DicitonaryFeignClient;
import org.xiaoli.xiaolicommoncore.utils.BeanCopyUtil;
import org.xiaoli.xiaolicommoncore.utils.JsonUtil;
import org.xiaoli.xiaolicommonredis.service.RedisService;
import org.xiaoli.xiaoliportalservice.homepage.domain.dto.DicDataDTO;
import org.xiaoli.xiaoliportalservice.homepage.service.IDictionaryService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class DictionaryServiceImpl implements IDictionaryService {


    private static final String DICT_TYPE_PREFIX = "applet:dict:type:";
    private static final Long DICT_TYPE_TIMEOUT = 5L;
    private static final String DICT_DATA_PREFIX = "applet:dict:data:";
    private static final Long DICT_DATA_TIMEOUT = 5L;

    @Autowired
    private DicitonaryFeignClient dicitonaryFeignClient;

    @Autowired
    private RedisService redisService;


    /**
     * 根据字典数据类型来查询字典数据列表
     * @param types
     * @return
     */
    @Override
    public Map<String, List<DicDataDTO>> batchFindDictionaryDataByType(List<String> types) {


        Map<String, List<DicDataDTO>> result = new HashMap<>();

        List<String> notCacheTypes = new ArrayList<>();


        //从缓存中获取数据，这些数据肯定就不需要再变化了~~
        //type1 : [data1,data2....]
        //type2 : [data1,data2....]
        for(String type : types){
            List<DicDataDTO> dataDTOList = getCacheList(type);
            if(CollectionUtils.isEmpty(dataDTOList)){
                notCacheTypes.add(type);
            }else{
                result.put(type, dataDTOList);
            }
        }

        //全部存在：返回
        if(CollectionUtils.isEmpty(notCacheTypes)){
            return result;
        }

        //不存在：feign接口
        Map<String, List<DictionaryDataDTO>> stringListMap =
                dicitonaryFeignClient.selecDataByTypes(notCacheTypes);

        if(null == stringListMap){
            log.error("字典类型不存在，notCacheTypes:{}", JsonUtil.obj2String(notCacheTypes));
            return result;
        }

        //缓存结果

        //1.对于哈希表遍历的方法，需要再熟悉熟悉
        for(Map.Entry<String,List<DictionaryDataDTO>> entry : stringListMap.entrySet()){
            List<DicDataDTO> dataDTOList = BeanCopyUtil.copyList(entry.getValue(), DicDataDTO::new);
            cacheList(entry.getKey(),dataDTOList);
            result.put(entry.getKey(),dataDTOList);
        }
        return result;
    }

    /**
     * 根据字典数据的keys获取字典数据
     * @param dataKeys
     * @return
     */
    @Override
    public Map<String, DicDataDTO> batchFindDictionaryData(List<String> dataKeys) {

        List<String> notCacheDataKeys = new ArrayList<>();
        Map<String, DicDataDTO> result = new HashMap<>();
        //1.查缓存
        for(String dataKey : dataKeys){
            DicDataDTO dicDataDTO = getDataCache(dataKey);
            if(null == dicDataDTO){
                notCacheDataKeys.add(dataKey);
            }else{
                result.put(dataKey,dicDataDTO);
            }
        }

        //存在:返回
        if(CollectionUtils.isEmpty(notCacheDataKeys)){
            return result;
        }

        //不存在：feign
        List<DictionaryDataDTO> dicDataByKeys = dicitonaryFeignClient.getDicDataByKeys(notCacheDataKeys);
        if(dicDataByKeys.isEmpty()){
            log.error("feign字典数据不存在！noCacheDataKeys:{}", JsonUtil.obj2String(dicDataByKeys));
            return result;
        }

        //缓存结果
        for(DictionaryDataDTO dictionaryDataDTO : dicDataByKeys){
            DicDataDTO dataDTO = new DicDataDTO();
            BeanUtil.copyProperties(dictionaryDataDTO,dataDTO);
            cacheData(dictionaryDataDTO.getDataKey(),dataDTO);
            result.put(dictionaryDataDTO.getDataKey(),dataDTO);

        }
        return result;
    }

//  ---------------------------------------------------------------------------------------------
    private void cacheData(String dataKey, DicDataDTO dataDTO) {
        if(StringUtils.isBlank(dataKey)){
            return;
        }
        redisService.setCacheObject(DICT_DATA_PREFIX+dataKey,JsonUtil.obj2String(dataDTO),
                DICT_DATA_TIMEOUT,TimeUnit.SECONDS);
    }

    private DicDataDTO getDataCache(String dataKey) {

        String str = redisService.getCacheObject(DICT_DATA_PREFIX + dataKey, String.class);
        if(StringUtils.isBlank(str)){
            return null;
        }
        return JsonUtil.string2Obj(str,DicDataDTO.class);
    }


//  ------------------------------------------------------------------------------------------
    private void cacheList(String key, List<DicDataDTO> dataDTOList) {
        if(StringUtils.isEmpty(key)){
            return;
        }
        redisService.setCacheObject(DICT_TYPE_PREFIX + key
                ,JsonUtil.obj2String(dataDTOList)
                ,DICT_TYPE_TIMEOUT, TimeUnit.MINUTES);
    }

    private List<DicDataDTO> getCacheList(String type) {
        if(StringUtils.isEmpty(type)){
            return null;
        }

        String str = redisService.getCacheObject(DICT_TYPE_PREFIX + type, String.class);
        if(StringUtils.isEmpty(str)){
            return null;
        }
        return JsonUtil.string2List(str, DicDataDTO.class);
    }
}
