package org.xiaoli.xiaolichatservice.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xiaoli.xiaolicommonredis.service.RedisService;

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
























}
