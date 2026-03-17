package org.xiaoli.xiaolichatservice.config;


import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

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





}
