package org.xiaoli.xiaoliadminservice.config.service;


import org.xiaoli.xiaoliadminapi.config.domain.dto.ArgumentAddReqDTO;
import org.xiaoli.xiaoliadminapi.config.domain.dto.ArgumentDTO;
import org.xiaoli.xiaoliadminapi.config.domain.dto.ArgumentEditReqDTO;
import org.xiaoli.xiaoliadminapi.config.domain.dto.ArgumentListReqDTO;
import org.xiaoli.xiaoliadminapi.config.domain.vo.ArgumentVO;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.vo.BasePageVO;

import java.util.List;

public interface ISysArgumentService {

    /**
     * 新增参数
     * @param argumentAddReqDTO 新增参数请求DTO
     * @return Long
     */
    R<Long> add(ArgumentAddReqDTO argumentAddReqDTO);

    /**
     * 查看参数列表
     * @param argumentListReqDTO
     * @return
     */
    BasePageVO<ArgumentVO> list(ArgumentListReqDTO argumentListReqDTO);

    Long editArgument(ArgumentEditReqDTO argumentEditReqDTO);

    ArgumentDTO getByConfigKey(String configKey);

    List<ArgumentDTO> getByConfigKeys(List<String> configKeys);
}
