//package org.xiaoli.xiaolicommonmessage.config;
//
//
//import com.aliyun.dypnsapi20170525.Client;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.aliyun.teaopenapi.models.Config;
//
///**
// * 阿里云短信配置服务（普通短信服务）
// */
//@Configuration
//@RefreshScope
//public class AliSmsConfig {
//
//    /**
//     * accessKeyId
//     */
//    @Value("${sms.aliyun.accessKeyId:}")
//    private String accessKeyId;
//
//
//    /**
//     * accessSecret
//     */
//    @Value("${sms.aliyun.accessSecret:}")
//    private String accessSecret;
//
//
//    /**
//     * endpoint
//     */
//    @Value("${sms.aliyun.endpoint:dysmsapi.aliyuncs.com}")
//    private String endpoint;
//
//    /**
//     * 注册客户端
//     * @return Client
//     * @throws Exception 客户端创建异常
//     */
//    @Bean("aliClient")
//    public Client client() throws Exception {
//        Config config = new Config()
//                .setAccessKeyId(accessKeyId)
//                .setAccessKeySecret(accessSecret)
//                .setEndpoint(endpoint);
//        return new Client(config);
//    }
//}
