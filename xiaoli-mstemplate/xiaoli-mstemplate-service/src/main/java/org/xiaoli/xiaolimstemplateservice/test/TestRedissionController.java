package org.xiaoli.xiaolimstemplateservice.test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoli.xiaolicommonredis.service.RedisService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * 手动实现分布式锁，通过setnx~~~
 */

@RestController
@Slf4j
@RequestMapping("/test/redis")
public class TestRedissionController {

    @Autowired
    private RedisService redisService;


    //问题一: 锁未设置有效时间  //  如果A线程卡死不能释放掉锁,会导致其他线程的业务逻辑出现异常
    //问题二：减库存操作时非原子（非主要矛盾）  // 这无关紧要
    //问题三：A线程释放了B线程的锁 ~~ 这步操作可以通过UUID来进行解决
    //问题四：解锁逻辑是非原子操作，可以使用lua脚本进行处理 ~~ 通过lua脚本,可以使解锁成为原子性的
    //问题五: 锁的有效时间很难设置一个合理有效的时间,~~ 看门狗 ,来进行锁的有效时间延长

    @PostMapping("/delStock")
    public String delStock(){

        String uuid = UUID.randomUUID().toString();
        String proKey = "proKey";
        Boolean save = redisService.setCacheObjectIfAbsent(proKey, uuid,30L, TimeUnit.SECONDS);
//      判断是否拿到锁了没
        if(!save){
            return "没有拿到锁";
        }
//      执行业务逻辑
        try{
            String stockKey = "stock";
            Integer stock = redisService.getCacheObject(stockKey, Integer.class);

            if(stock <=0){
                return "库存没有了，秒杀失败！";
            }
            stock--;
            redisService.setCacheObject(stockKey, stock);
        }finally {
//          使用Lua脚本进行解锁，这个操作就是原子性的
            redisService.cad(proKey, uuid);
        }
        return "ok";
    }


//
}
