package org.xiaoli.xiaolichatservice.service;

import org.xiaoli.xiaolichatservice.domain.dto.*;
import org.xiaoli.xiaolichatservice.domain.vo.MessageVO;

import java.util.List;

public interface IMessageService {

    /**
     * 根据消息ID获取消息信息
     * @param messageId
     * @return
     */
    MessageDTO get(Long messageId);


    /**
     * 新增一条消息
     * @param messageSendReqDTO
     * @return
     */
    boolean add(MessageSendReqDTO messageSendReqDTO);



    /**
     * 查询聊天信息列表
     * @param messageListReqDTO
     * @return
     */
    List<MessageVO> list(MessageListReqDTO messageListReqDTO);



    /**
     * 更新消息访问状态
     *
     * @param reqDTO
     * @return
     */
    void batchVisited(MessageVisitedReqDTO reqDTO);

    /**
     * 更新消息已读状态（目前只有语音）
     *
     * @param reqDTO
     * @return
     */
    void batchRead(MessageReadReqDTO reqDTO);
}
