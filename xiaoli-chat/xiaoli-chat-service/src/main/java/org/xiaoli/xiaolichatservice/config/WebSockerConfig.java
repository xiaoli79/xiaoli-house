package org.xiaoli.xiaolichatservice.config;


import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.List;
import java.util.Map;

@Configuration
public class WebSockerConfig extends ServerEndpointConfig.Configurator {


    /**
     * 这个类注册每个家了@ServerEndpoint的spring bean 节点，算是spring整合websocket的体现
     * 没有的话会报404
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


    /**
     * 建立握手时，连接之前的操作，可以获取源信息
     * @param sec
     * @param request
     * @param response
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response){

        //设置用户信息
        Map<String, List<String>> headers = request.getHeaders();
        List<String> authorization = headers.get("Authorization");

        if(CollectionUtils.isEmpty(authorization)){
            //建立连接时，请求头的用户信息为空，无法通过校验
            //管理连接请求要求Authorization参数必传
            throw new RuntimeException("请求头不符合要求，缺少Authorization信息");
        }
        final Map<String,Object> userProperties = sec.getUserProperties();
        userProperties.put("Authorization",authorization.get(0));
    }
}