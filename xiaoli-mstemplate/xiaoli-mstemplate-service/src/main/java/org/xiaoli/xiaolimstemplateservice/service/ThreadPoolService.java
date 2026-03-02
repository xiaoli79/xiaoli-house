package org.xiaoli.xiaolimstemplateservice.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ThreadPoolService{


//  加上这个注解就是可以启动多线程~~
    @Async("threadPoolTaskExecutor")
    public void info() {

        log.info("ThreadPoolService thread name{}",Thread.currentThread().getName());



    }
}
