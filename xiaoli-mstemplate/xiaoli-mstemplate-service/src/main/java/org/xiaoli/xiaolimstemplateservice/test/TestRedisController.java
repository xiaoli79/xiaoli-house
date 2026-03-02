package org.xiaoli.xiaolimstemplateservice.test;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommonredis.service.RedisService;
import org.xiaoli.xiaolimstemplateservice.domain.TestClass;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/test/redis")
public class TestRedisController {


    @Autowired
    private RedisService redisService;


    @PostMapping("/add")
    public R<Void> add(){
//
//        redisService.setCacheObject("test","abc");
//        redisService.setCacheObject("testAGC","abc",15L, TimeUnit.SECONDS);
//
//        redisService.setCacheObjectIfAbsent("test",123);
//        TestClass testClass = new TestClass();
//        testClass.setName("test");
//        testClass.setAge(123);
//        testClass.setId(123);
//        List<Map<String,TestClass>> list = new ArrayList<>();
//        Map<String,TestClass> map = new HashMap<>();
//        map.put("test", testClass);
//        list.add(map);
//        redisService.setCacheObject("testList",list);

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        redisService.setCacheList("list", list);

        return R.ok();
    }


    @PostMapping("/insert")
    public R<Void> insert(){
        List<String> list = new ArrayList<>();
        list.add("6");
        redisService.leftPushForList("list",list);
        return R.ok();
    }

    @GetMapping("/get")
    public R<Void> get(){
//        String str = redisService.getCacheObject("test", String.class);
//        log.info(str);

//      将数据从Redis中取出来,确保取出来的数据不会出现泛型擦除问题~~~~~
//        List<Map<String, TestClass>> testList = redisService.getCacheObject("testList", new TypeReference<List<Map<String, TestClass>>>() {
//        });
        List<String> list = redisService.getCacheList("list", String.class);
        System.out.println(list);

//
//        System.out.println(testList);
        return R.ok();
    }

    @PostMapping("/delete")
    public R<Void> delete(){
        redisService.removeForAllList("list");
        return R.ok();
    }



    @PostMapping("/set/add")
    public R<Void> setAdd(){
        redisService.addMember("list",1,2,3,1,2,3,4,5,6);

        return R.ok();

    }


    @PostMapping("/zset/add")
    public R<Void> zsetAdd(){
        redisService.addMemberZSet("testZset","a",1);
        redisService.addMemberZSet("testZset","b",2);
        redisService.addMemberZSet("testZset","c",3);
        redisService.addMemberZSet("testZset","d",4);

        return R.ok();

    }


    @PostMapping("/hash/add")
    public R<Void> hashAdd(){
        Map<String,Object> map = new HashMap<>();
        map.put("test1",3);
        map.put("test2",2);
        map.put("test3",1);
        redisService.setCacheMap("Test",map);

        return R.ok();
    }



    @PostMapping("/set/delete")
    public R<Void> setDelete(){
        redisService.deleteMember("list",1,2,3);
        return R.ok();
    }


    @GetMapping("/hash/get")
    public R<Void> getData(){
        Map<String, Integer> test = redisService.getCacheMap("Test", new TypeReference<Map<String, Integer>>() {
        });


//      这是以键值对的类型进行输出也就是map~~
        System.out.println(test);



        List<String> hkeys = new ArrayList<>();
        hkeys.add("test2");
        hkeys.add("test1");


//

        List<String> multiCacheMapValue = redisService.getMultiCacheMapValue("Test", hkeys, new TypeReference<List<String>>() {
        });


//      这是以List<String>
        System.out.println(multiCacheMapValue);
        return R.ok();

    }
}
