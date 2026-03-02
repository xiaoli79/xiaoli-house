package org.xiaoli.xiaoliportalservice.user;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * 应用程序启动类
 */


@SpringBootApplication
@Slf4j
//本项目中没有mapper不能够扫描，除非在pom.xml添加别的模块的依赖就可以~~
//@MapperScan("org.xiaoli.**.mapper")
@EnableFeignClients(basePackages = {"org.xiaoli.**.feign"})
public class XiaoLiPortalServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(XiaoLiPortalServiceApplication.class, args);
        log.info("门户服务启动成功");
    }
}
