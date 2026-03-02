package org.xiaoli.xiaoliadminapi.config.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.xiaoli.xiaoliadminapi.config.domain.dto.DictionaryDataDTO;
import java.util.List;
import java.util.Map;

/**
 * 字典服务的远程调用
 */

@FeignClient(contextId = "dictionaryFeignClient",value = "bite-admin")
public interface DicitonaryFeignClient {

    /**
     * 获取某个字典类型的所有字典数据
     * @param typeKey 字典类型键
     * @return
     */
    @GetMapping("/dictionary_data/type")
    List<DictionaryDataDTO> selecDataByType(@RequestParam String typeKey);


    /**
     * 获取多个字典类型下的所有字典数据
     * @param typeKeys  多个字典类型
     * @return 哈希，key对应的是字典类型的建，value对应的是字典类型下的所有字典数据
     */
    @PostMapping("/dictionary_data/types")
    Map<String,List<DictionaryDataDTO>> selecDataByTypes(@RequestBody List<String> typeKeys);


    /**
     * 根据字典数据业务主键获取字典数据对象
     * @param dataKey 字典数据业务主键
     * @return 字典数据对象
     */
    @GetMapping("/dictionary_data/key")
    DictionaryDataDTO getDicDataByKey (@RequestParam String dataKey);

    /**
     * 根据多个字典数据的业务主键获取多个字典数据对象
     * @param dataKeys
     * @return
     */
    @PostMapping("/dictionary_data/keys/")
    List<DictionaryDataDTO> getDicDataByKeys (@RequestBody List<String> dataKeys);

}
