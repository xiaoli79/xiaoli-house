package org.xiaoli.xiaolichatservice;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;


@Slf4j
@MapperScan("org.xiaoli.**.mapper")
@EnableFeignClients(basePackages = {"org.xiaoli.**.feign"})
@SpringBootApplication
public class XiaoLiChatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoLiChatServiceApplication.class, args);
        log.info("咨询服务启动成功");
    }
}
