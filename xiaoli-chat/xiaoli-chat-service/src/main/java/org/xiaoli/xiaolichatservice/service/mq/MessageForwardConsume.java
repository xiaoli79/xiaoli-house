package org.xiaoli.xiaolichatservice.service.mq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;
import org.xiaoli.xiaolichatservice.config.RabbitMqConfig;
import org.xiaoli.xiaolichatservice.domain.dto.MessageSendReqDTO;
import org.xiaoli.xiaolichatservice.domain.dto.WebSocketDTO;
import org.xiaoli.xiaolichatservice.domain.enums.WebSocketDataTypeEnum;
import org.xiaoli.xiaolichatservice.service.websocket.WebSocketServer;
import org.xiaoli.xiaolicommoncore.utils.JsonUtil;


/**
 * 推送消息给目标用户
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
public class MessageForwardConsume {


    @RabbitHandler
    public void process(MessageSendReqDTO messageSendReqDTO){

        try{
            WebSocketDTO<String> webSocketDTO = new WebSocketDTO<>();
            webSocketDTO.setType(WebSocketDataTypeEnum.CHAT.getType());
            webSocketDTO.setData(JsonUtil.obj2String(messageSendReqDTO));
            // 支持消息丢弃，判断当前服务器是否维持了目标的连接~
            WebSocketServer.sendMessage(messageSendReqDTO.getToId(),webSocketDTO);
        }catch (Exception e){
            log.error("聊天消息转发失败：{}",messageSendReqDTO,e);

        }
    }
}
