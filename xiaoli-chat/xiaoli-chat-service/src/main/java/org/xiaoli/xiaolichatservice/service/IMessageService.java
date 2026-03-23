package org.xiaoli.xiaolichatservice.service;

import org.xiaoli.xiaolichatservice.domain.dto.MessageDTO;
import org.xiaoli.xiaolichatservice.domain.dto.MessageSendReqDTO;

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
}
