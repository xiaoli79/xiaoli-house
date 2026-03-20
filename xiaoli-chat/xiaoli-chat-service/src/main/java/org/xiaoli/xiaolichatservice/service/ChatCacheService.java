package org.xiaoli.xiaolichatservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xiaoli.xiaolichatservice.domain.dto.MessageDTO;
import org.xiaoli.xiaolichatservice.domain.dto.SessionStatusDetailDTO;
import org.xiaoli.xiaolicommoncore.utils.JsonUtil;
import org.xiaoli.xiaolicommonredis.service.RedisService;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
@Slf4j
public class ChatCacheService {

    @Autowired
    private RedisService redisService;

    //用户id - 会话id
    private static final String CHAT_ZSET_USER_PREFIX = "chat:zset:user:";


    // 会话id - 会话详细信息DTO
    private static final String CHAT_SESSION_PREFIX = "chat:session:";


    // 会话id - 聊天信息列表(zset)
    private static  final String CHAT_ZSET_SESSION_PREFIX = "chat:zset:session:";


    /**
     * 新增用户下的一个新对话
     * @param userId
     * @param sessionId
     * @param lastSessionTime  排序规则，最后的会话时间
     */
    public void addUserSessionToCache(Long userId,Long sessionId,Long lastSessionTime){

        try{
            String key = CHAT_ZSET_USER_PREFIX + userId;
            redisService.addMemberZSet(key,sessionId,lastSessionTime);
        } catch (Exception e){
            log.error("新增用户下的新会话id缓存时发生异常，userId:{}",userId,e);
        }
    }

    /**
     *获取用户下的会话列表
     *
     * @param userId
     * @return
     */
    public Set<Long> getUserSessionByCache(Long userId){
        Set<Long> sessionIds = new LinkedHashSet<>();
        try{
            String key = CHAT_ZSET_USER_PREFIX + userId;
            sessionIds = redisService.getCacheZSetDesc(key, new TypeReference<LinkedHashSet<Long>>() {});

            if(CollectionUtils.isEmpty(sessionIds)){
                return new HashSet<>();
            }
        }catch (Exception e){
            log.error("从患处那种获取用户下的会话列表异常,userId:{}",userId,e);
        }
        return sessionIds;
    }


    /**
     * 缓存会话详细信息
     * @param sessionId
     * @param sessionStatusDetailDTO
     */
    public void cahceSessionDTO(Long sessionId, SessionStatusDetailDTO sessionStatusDetailDTO){
        try{
            String key = CHAT_SESSION_PREFIX + sessionId;
            redisService.setCacheObject(key, JsonUtil.obj2String(sessionStatusDetailDTO));
        }catch (Exception e){
            log.error("缓存会话详细信息时发生异常,sessionId:{}",sessionId,e);
        }
    }


    /**
     * 获取会话详细信息缓存
     * @param sessionId
     * @return
     */
    public SessionStatusDetailDTO getSessionDTOByCache(Long sessionId){
        SessionStatusDetailDTO sessionStatusDetailDTO = new SessionStatusDetailDTO();
        try{
            String key = CHAT_SESSION_PREFIX + sessionId;
            String str = redisService.getCacheObject(key, String.class);
            if(StringUtils.isEmpty(str)){
                return new SessionStatusDetailDTO();
            }
            sessionStatusDetailDTO =  JsonUtil.string2Obj(str, SessionStatusDetailDTO.class);
        }catch (Exception e){
            log.error("获取会话详细信息失败，sessionId:{}",sessionId,e);
        }
        return sessionStatusDetailDTO;
    }


    /**
     * 新增会话下的消息缓存
     * @param sessionId
     * @param messageDTO
     */
    public void addMessageToCache(Long sessionId,MessageDTO messageDTO){
        try{

            String key = CHAT_ZSET_SESSION_PREFIX + sessionId;
            redisService.addMemberZSet(key,messageDTO,Long.parseLong(messageDTO.getMessageId()));

        }catch (Exception e){
            log.error("新增会话下的消息缓存发生异常,sessionId:{}",sessionId,e);
        }
    }


    /**
     * 获取会话下的聊天记录列表
     * @param sessionId
     * @return
     */
    public Set<MessageDTO> getMessageDTOSByCache(Long sessionId){

        Set<MessageDTO> messageDTOS = new HashSet<>();

        try{
            String key  =CHAT_ZSET_SESSION_PREFIX + sessionId;
            messageDTOS = redisService.getCacheZSetDesc(key, new TypeReference<LinkedHashSet<MessageDTO>>() {
            });
            if(CollectionUtils.isEmpty(messageDTOS)){
                return new HashSet<>();
            }
        }catch (Exception e){
            log.error("获取回下下的消息列表缓存发生异常，sessionId:{}",sessionId,e);
        }
        return messageDTOS;
    }


    /**
     * 删除会话记录的消息缓存
     * @param sessionId
     * @param messageId
     */
    public void removeMessageDTOCache(Long sessionId,String messageId){
        try{
            String key = CHAT_ZSET_SESSION_PREFIX + sessionId;

            //根据排序分支来进行删除
            redisService.removeZSetByScore(key,Long.parseLong(messageId),Long.parseLong(messageId));
        }catch (Exception e){
            log.error("删除会话在的指定消息缓存发生异常,sessionId:{},messageId:{}",sessionId,messageId,e);
        }
    }

}
