package org.xiaoli.xiaoliadminservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class XiaoliAdminServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoliAdminServiceApplication.class, args);
        log.info("基础管理服务启动成功");
    }
}
