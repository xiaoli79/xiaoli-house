package org.xiaoli.xiaolichatservice.service;

import org.xiaoli.xiaolichatservice.domain.dto.SessionAddReqDTO;
import org.xiaoli.xiaolichatservice.domain.vo.SessionAddResVO;

public interface ISessionService {



    /**
     * 新建会话
     * @param sessionAddReqDTO
     * @return
     */
    SessionAddResVO add(SessionAddReqDTO sessionAddReqDTO);
}
