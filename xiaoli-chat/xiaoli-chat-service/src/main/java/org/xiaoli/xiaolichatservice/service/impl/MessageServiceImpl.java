package org.xiaoli.xiaolichatservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoli.xiaolichatservice.domain.dto.MessageDTO;
import org.xiaoli.xiaolichatservice.domain.dto.MessageSendReqDTO;
import org.xiaoli.xiaolichatservice.domain.dto.SessionStatusDetailDTO;
import org.xiaoli.xiaolichatservice.domain.enums.MessageStatusEnum;
import org.xiaoli.xiaolichatservice.domain.enums.MessageTypeEnum;
import org.xiaoli.xiaolichatservice.entity.Message;
import org.xiaoli.xiaolichatservice.entity.Session;
import org.xiaoli.xiaolichatservice.mapper.MessageMapper;
import org.xiaoli.xiaolichatservice.mapper.SessionMapper;
import org.xiaoli.xiaolichatservice.service.ChatCacheService;
import org.xiaoli.xiaolichatservice.service.IMessageService;
import org.xiaoli.xiaolichatservice.service.SnowflakeIdService;
import org.xiaoli.xiaolicommoncore.utils.BeanCopyUtil;
import org.xiaoli.xiaolicommonsecurity.service.TokenService;

import java.util.Set;


@Service
@Slf4j
public class MessageServiceImpl implements IMessageService {


    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SessionMapper sessionMapper;
    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Autowired
    private ChatCacheService chatCacheService;

    @Autowired
    private TokenService tokenService;


    @Override
    public MessageDTO get(Long messageId) {

        if(null == messageId){
            return null;
        }


        //查询mysql
        Message message = messageMapper.selectById(messageId);
        if(null == message){
            return null;
        }
        MessageDTO messageDTO = new MessageDTO();
        BeanCopyUtil.copyProperties(message,messageDTO);
        messageDTO.setMessageId(String.valueOf(message.getId()));
        return messageDTO;
    }

    @Override
    public boolean add(MessageSendReqDTO reqDTO) {

        //1.参数校验
        if(null == MessageTypeEnum.getByCode(reqDTO.getType())){
            log.error("新增消息时，消息列表有误！type:{}",reqDTO.getType());
            return false;
        }

        //校验sessionId的合法性
        Session session = sessionMapper.selectById(reqDTO.getSessionId());
        if(null == session){
            log.error("新增信息时，会话不存在！sesisonId:{}",reqDTO.getSessionId());
            return false;
        }

        //新增聊天消息
        Message message = new Message();
        message.setId(null == reqDTO.getMessageId() ? snowflakeIdService.nextId() : reqDTO.getMessageId());
        message.setFromId(reqDTO.getFromId());
        message.setSessionId(reqDTO.getSessionId());
        message.setType(reqDTO.getType());
        message.setContent(StringUtils.isEmpty(reqDTO.getContent()) ? "" : reqDTO.getContent());
        message.setStatus(null == reqDTO.getStatus() ? MessageStatusEnum.MESSAGE_UNREAD.getCode() : reqDTO.getStatus());
        message.setVisited(null == reqDTO.getVisited() ? MessageStatusEnum.MESSAGE_NOT_VISITED.getCode() : reqDTO.getVisited());
        message.setCreateTime(Long.valueOf(reqDTO.getCreateTime()));
        messageMapper.insert(message);


        //新增缓存：会话id下的消息列表
        MessageDTO messageDTO = new MessageDTO();
        BeanCopyUtil.copyProperties(message,messageDTO);
        messageDTO.setMessageId(String.valueOf(message.getId()));
        chatCacheService.addMessageToCache(message.getSessionId(),messageDTO);

        //更新缓存：会话详细信息
        SessionStatusDetailDTO sessionDTO = chatCacheService.getSessionDTOByCache(message.getSessionId());
        assert null != sessionDTO;
        //设置对方消息的未浏览数
        SessionStatusDetailDTO.UserInfo toUserInfo = sessionDTO.getToUser(reqDTO.getFromId());
        toUserInfo.setNotVisitedCount(toUserInfo.getNotVisitedCount() + 1);
        sessionDTO.setLastSessionTime(message.getCreateTime());
        sessionDTO.setLastMessageDTO(messageDTO);
        //必须是卡片消息，才会设置属性
        if(MessageTypeEnum.MESSAGE_CARD.getCode().equals(reqDTO.getType())){
            Set<Long> houseIds = sessionDTO.getHouseIds();
            String houseId = JSONObject.parseObject(reqDTO.getContent())
                    .getString("houseId");
            houseIds.add(Long.parseLong(houseId));
            sessionDTO.setHouseIds(houseIds);
        }
        chatCacheService.cahceSessionDTO(message.getSessionId(),sessionDTO);

        // 更新或新增：用户下的会话列表  //新增用户会话列表
        // 针对两个用户
        chatCacheService.addUserSessionToCache(
                session.getUserId1(), session.getId(), sessionDTO.getLastSessionTime());
        chatCacheService.addUserSessionToCache(
                session.getUserId2(), session.getId(), sessionDTO.getLastSessionTime());
        return true;
    }
}
