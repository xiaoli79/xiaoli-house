package org.xiaoli.xiaolichatservice.service.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xiaoli.xiaolichatservice.config.ServerEncoder;
import org.xiaoli.xiaolichatservice.config.WebSockerConfig;
import org.xiaoli.xiaolichatservice.domain.dto.WebSocketDTO;
import org.xiaoli.xiaolichatservice.domain.enums.WebSocketDataTypeEnum;
import org.xiaoli.xiaolicommoncore.utils.JsonUtil;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;
import org.xiaoli.xiaolicommonsecurity.domain.dto.LoginUserDTO;
import org.xiaoli.xiaolicommonsecurity.service.TokenService;
import org.xiaoli.xiaolicommonsecurity.utils.SecurityUtil;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket",
        configurator = WebSockerConfig.class,
        encoders = {ServerEncoder.class})
@Component
@Slf4j
public class WebSocketServer {




    

    //WebSocketServer不能直接通过@Autowired进行注入
    // 不能使用 @Autowired 或 @Resource
    // 因为 ws 是通过 WebSocketConfig.getEndpointInstance() 方法来获取每个连接对应的调用对象
    // 而getEndpointInstance默认是通过反射来构造的，而不是 Spring 容器获取连接对象

    //建立连接后的会话对象
    private Session session;

    /**
     * 存放服务器和每个客户端对应的WebSocket对象
     * 建立连接之后去设值，断开连接之后需要删除
     */
    private static ConcurrentHashMap<Long, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    private Long userId;


    //这是用来查找用户信息
    private static TokenService tokenService;



    /**
     * Autowired 注解作用与方法
     * 当类实例化时，Spring容器会自动解析方法的参数。并为参数找到与其匹配的Bean实例，然后调用这个方法并注入
     * @param tokenService
     * @return
     */
    @Autowired
    public TokenService setTokenService(TokenService tokenService) {
        return WebSocketServer.tokenService = tokenService;
    }


    @OnOpen
    public void onOpen(Session session) throws IOException {

        try{
            String token =(String) session.getUserProperties().get("Authorization");
            token = SecurityUtil.replaceTokenPrefix(token);

            if(null == token){
                log.error("没有传递用户的token信息");
                throw new ServiceException("没有传递用户的token信息", ResultCode.INVALID_PARA.getCode());
            }

            LoginUserDTO loginUserDTO = tokenService.getLoginUser(token);

            if(null == loginUserDTO || null == loginUserDTO.getUserId()){
                throw new ServiceException("用户token有误",ResultCode.INVALID_PARA.getCode());
            }

            this.session = session;
            this.userId = loginUserDTO.getUserId();

            webSocketMap.put(userId,this);
            log.info("用户{}已经连接",userId);

            log.info("连接成功");
            this.session = session;

        }catch (Exception e){
            log.error("连接出现异常，关闭连接",e);
            session.close();
        }
    }

    @OnClose
    public void onClose() {
        if(userId != null &&  webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
        }
        log.info("用户{}已经关闭连接",userId);
        this.session = null;
        this.userId = null;
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