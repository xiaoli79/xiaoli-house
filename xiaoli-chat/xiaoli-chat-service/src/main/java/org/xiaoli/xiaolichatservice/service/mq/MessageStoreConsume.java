package org.xiaoli.xiaolichatservice.service.mq;


import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xiaoli.xiaolichatservice.config.RabbitMqConfig;
import org.xiaoli.xiaolichatservice.domain.dto.MessageSendReqDTO;
import org.xiaoli.xiaolichatservice.service.IMessageService;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;
import org.xiaoli.xiaolicommonredis.service.RedissonLockService;



/**
 * 持久化聊天消息
 */
@Component
@Slf4j
@RabbitListener(bindings ={
        @QueueBinding(
                value =  @Queue(),
                exchange = @Exchange(
                        value = RabbitMqConfig.EXCHANGE_NAME,
                        type = ExchangeTypes.FANOUT
                )
        )
} )
public class MessageStoreConsume {

    private static final String LOCK_KEY = "chat:db:lock";

    @Autowired
    private RedissonLockService redissonLockService;

    @Autowired
    private IMessageService messageService;



    @RabbitHandler
    public void process(MessageSendReqDTO messageSendReqDTO) {


        //1.获取分布式锁
        RLock rlock = redissonLockService.acquire(LOCK_KEY, -1);
        if(null == rlock){
            //1.获取锁失败，跳过执行
            return ;
        }


        try{
            //2.幂等性处理：消息已存在，不处理
            if(null != messageService.get(messageSendReqDTO.getMessageId())){
                return;
            }

            //3.不存在：持久化存储
            if(!messageService.add(messageSendReqDTO)){
                throw new ServiceException("消息持久化失败");
            }



        }catch (Exception e){
            log.error("消息持久化异常:{}",messageSendReqDTO,e);

        }finally {
            //释放锁
            if(rlock.isLocked() && rlock.isHeldByCurrentThread()){
                redissonLockService.releaseLock(rlock);
            }

        }

















    }







}
