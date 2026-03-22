package org.xiaoli.xiaolichatservice.service;

import org.xiaoli.xiaolichatservice.domain.dto.SessionAddReqDTO;
import org.xiaoli.xiaolichatservice.domain.dto.SessionGetReqDTO;
import org.xiaoli.xiaolichatservice.domain.dto.SessionListReqDTO;
import org.xiaoli.xiaolichatservice.domain.vo.SessionAddResVO;
import org.xiaoli.xiaolichatservice.domain.vo.SessionGetResVO;

import java.util.List;

public interface ISessionService {



    /**
     * 新建会话
     * @param sessionAddReqDTO
     * @return
     */
    SessionAddResVO add(SessionAddReqDTO sessionAddReqDTO);



    /**
     * 查询咨询会话
     * @param sessionGetReqDTO
     * @return
     */
    SessionGetResVO get(SessionGetReqDTO sessionGetReqDTO);


    /**
     * 获取会话列表
     * @param sessionListReqDTO
     * @return
     */
    List<SessionGetResVO> list(SessionListReqDTO sessionListReqDTO);
}
