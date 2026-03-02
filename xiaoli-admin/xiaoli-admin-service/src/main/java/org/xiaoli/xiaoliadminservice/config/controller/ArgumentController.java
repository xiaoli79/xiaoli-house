package org.xiaoli.xiaoliadminservice.config.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiaoli.xiaoliadminapi.config.domain.dto.ArgumentAddReqDTO;
import org.xiaoli.xiaoliadminapi.config.domain.dto.ArgumentDTO;
import org.xiaoli.xiaoliadminapi.config.domain.dto.ArgumentEditReqDTO;
import org.xiaoli.xiaoliadminapi.config.domain.dto.ArgumentListReqDTO;
import org.xiaoli.xiaoliadminapi.config.domain.vo.ArgumentVO;
import org.xiaoli.xiaoliadminapi.config.feign.ArgumentFeignCLient;
import org.xiaoli.xiaoliadminservice.config.service.ISysArgumentService;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.vo.BasePageVO;

import java.util.List;

/**
 * 参数服务相关的接口
 */

@RestController
@RequestMapping("/argument")
public class ArgumentController implements ArgumentFeignCLient {



    @Autowired
    private ISysArgumentService sysArgumentService;


    /**
     * 新增参数
     * @param argumentAddReqDTO 新增参数请求DTO
     * @return Long
     */
    @PostMapping("/add")
    public R<Long> add(@RequestBody @Validated ArgumentAddReqDTO argumentAddReqDTO){
        return sysArgumentService.add(argumentAddReqDTO);
    }

    /**
     * 查看参数列表
     * @param argumentListReqDTO
     * @return
     */
    @GetMapping("/list")
    public R<BasePageVO<ArgumentVO>> list(@Validated ArgumentListReqDTO argumentListReqDTO){
        return R.ok(sysArgumentService.list(argumentListReqDTO));
    }


    /**
     * 编辑参数
     * @param argumentEditReqDTO
     * @return
     */
    @PostMapping("/edit")
    public R<Long> editArgument(@RequestBody @Validated ArgumentEditReqDTO argumentEditReqDTO){
        return R.ok(sysArgumentService.editArgument(argumentEditReqDTO));
    }


    /**
     * 根据参数键来查询参数的对象
     * @param configKey
     * @return
     */
    @Override
    public ArgumentDTO getByConfigKey(String configKey) {
        return sysArgumentService.getByConfigKey(configKey);
    }

    /**
     * 根据多个键来查询多个对象
     * @param configKeys
     * @return
     */
    @Override
    public List<ArgumentDTO> getByConfigKeys(List<String> configKeys) {
        return sysArgumentService.getByConfigKeys(configKeys);
    }
}
