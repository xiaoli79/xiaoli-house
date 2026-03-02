package org.xiaoli.xiaoliadminapi.config.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xiaoli.xiaoliadminapi.config.domain.dto.ArgumentDTO;

import java.util.List;

/**
 * 参数服务相关的远程调用
 */
@FeignClient(contextId = "argumentFeiginClient",value = "xiaoli-admin",path = "/argument")
public interface ArgumentFeignCLient {

    /**
     * 根据参数键来查询参数对象
     * @param configKey
     * @return
     */
    @GetMapping("/key")
    ArgumentDTO getByConfigKey(@RequestParam String configKey);


    /**
     * 根据多个参数键来查询多个参数对象
     * @param configKeys
     * @return
     */
    @GetMapping("/keys")
    List<ArgumentDTO> getByConfigKeys(@RequestParam List<String> configKeys);
}
