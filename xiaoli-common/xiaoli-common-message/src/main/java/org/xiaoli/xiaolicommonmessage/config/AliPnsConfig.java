package org.xiaoli.xiaolicommonmessage.config;

import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * 阿里云号码认证服务配置 (Phone Number Authentication Service)
 * 用于发送短信验证码
 */
@Configuration
@RefreshScope
public class AliPnsConfig {

    /**
     * accessKeyId
     */
    @Value("${pns.aliyun.accessKeyId:}")
    private String accessKeyId;

    /**
     * accessSecret
     */
    @Value("${pns.aliyun.accessKeySecret:}")
    private String accessSecret;

    /**
     * endpoint
     */
    @Value("${pns.aliyun.endpoint:}")
    private String endpoint;

    /**
     * 注册号码认证服务客户端
     * @return Client
     * @throws Exception 客户端创建异常
     */
    @Bean("aliPnsClient")
    public Client pnsClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessSecret)
                .setEndpoint(endpoint);
        return new Client(config);
    }
}
