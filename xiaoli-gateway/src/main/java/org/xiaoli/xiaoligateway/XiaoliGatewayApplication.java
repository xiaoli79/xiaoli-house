package org.xiaoli.xiaoligateway;

import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

@SpringBootApplication
public class XiaoliGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoliGatewayApplication.class, args);
    }

}
