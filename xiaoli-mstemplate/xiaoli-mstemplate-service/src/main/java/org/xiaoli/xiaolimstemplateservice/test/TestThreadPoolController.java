package org.xiaoli.xiaolimstemplateservice.test;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoli.xiaolimstemplateservice.service.ThreadPoolService;

@RestController
@Slf4j
@RequestMapping("/test/threadPool")

public class TestThreadPoolController {

    @Autowired
    private ThreadPoolService threadPoolService;

    @GetMapping("/info")
    public void info(){
        log.info("ThreadPoolController thread name{}",Thread.currentThread().getName());
        threadPoolService.info();
    }


}
