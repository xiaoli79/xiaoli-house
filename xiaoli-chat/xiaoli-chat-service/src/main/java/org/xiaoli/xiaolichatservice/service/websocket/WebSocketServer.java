package org.xiaoli.xiaolichatservice.service.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiaoli.xiaolichatservice.config.ServerEncoder;
import org.xiaoli.xiaolichatservice.config.WebSockerConfig;
import org.xiaoli.xiaolichatservice.domain.dto.WebSocketDTO;
import org.xiaoli.xiaolichatservice.domain.enums.WebSocketDataTypeEnum;
import org.xiaoli.xiaolicommoncore.utils.JsonUtil;


@ServerEndpoint(value = "/websocket",
        configurator = WebSockerConfig.class,
        encoders = {ServerEncoder.class})
@Component
@Slf4j
public class WebSocketServer {

    //建立连接后的会话对象
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        log.info("连接成功");
        this.session = session;
    }


    @OnClose
    public void onClose() {
        log.info("断开连接成功");
        this.session = null;
    }


    @OnError
    public void onError(Throwable throwable) {
        log.error("websocket出现连接异常");
        log.error(throwable.getMessage(), throwable);
    }


    /**
     * 服务器接收到的数据
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {

        //1.接收到消息
        log.info("接收到消息{}",message);

        try {
            //处理消息
            //⬇️将Json字符串转换为任意的对象
            WebSocketDTO<?> webSocketDTO = JsonUtil.string2Obj(message, WebSocketDTO.class);
            if(null== webSocketDTO){
                log.error("webSocket不支持的协议！messgae{}",message);
                return;
            }
            handleMessage(webSocketDTO.getType(),webSocketDTO.getData());

        } catch (Exception e) {
            log.error("消息推送失败"+e.getMessage());
        }
    }


    /**
     * 处理ws消息
     * @param type
     * @param data
     * @param <T>
     */
    private <T> void handleMessage(String type, T data) {

        WebSocketDataTypeEnum typeEnum = WebSocketDataTypeEnum.getByType(type);
        switch (typeEnum) {
            case TEXT:
                //处理文本消息(测试)
                handleTextMessage((String)data);
                break;
            case HEART_BEAT:
                //处理心跳消息
                handleHeartBeatMessage();
                break;
            case CHAT:
                //处理聊天消息
                handleChatMessage();
                break;
            default:
                //处理未知消息
                handleUnknowMessage(type);
                break;
        }
    }


    private void handleChatMessage() {


    }

    private void handleHeartBeatMessage() {

    }

    private  void handleUnknowMessage(String type) {

        log.error("无效的消息类型，无法处理! type:{}",type);
    }

    private void handleTextMessage(String data) {

        try{
            Thread.sleep(3000);
            String message = "服务端 "+data;
            sendMessage(new WebSocketDTO<>(WebSocketDataTypeEnum.TEXT.getType(), message));
        } catch (InterruptedException e) {
            log.error("处理文本消息异常!",e);
        }
    }


    /**
     * 给当前连接会话推送消息
     *
     * @param webSocketDTO
     */
    private void sendMessage(WebSocketDTO<?> webSocketDTO) {

        try{
            this.session.getBasicRemote().sendObject(webSocketDTO);
        } catch (Exception e) {
            log.error("ws消息推送失败，webSocketDTO:{}",JsonUtil.obj2String(webSocketDTO),e);
        }
    }
}