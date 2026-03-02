package org.xiaoli.xiaolicommoncache.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.xiaoli.xiaolicommonredis.service.RedisService;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CacheUtil {

    /**
     * 读取二级缓存
     * @param redisService redis服务
     * @param key  缓存key
     * @param typeReference 模板类型
     * @param cache 本地缓存服务
     * @return 缓存信息
     * @param <T> 缓存类型
     */
    public static <T> T getL2Cache(RedisService redisService, String key, TypeReference<T> typeReference,Cache<String,Object> cache) {
        T res = (T)cache.getIfPresent(key);
//       读取一级缓存
        if(res != null){
            log.info("读取本地缓存信息"+key);
            return res;
        }
//       读取二级缓存
        res= redisService.getCacheObject(key, typeReference);
        if(res != null){
            log.info("读取Redis缓存"+key);
//          写入一级缓存
            cache.put(key, res);
            return res;
        }
//       从mysql中处理，业务逻辑相差很大~~  具体情况要具体写，所以这段代码不在写了~~
        return null;
    }


    /**
     *
     * @param redisService redis服务
     * @param key 缓存key
     * @param value 缓存对象值
     * @param cache 本地缓存信息
     * @param timeout 超时时间
     * @param timeUnit 超时单位
     * @param <T>  对象类型
     */
    public static <T> void setL2Cache(RedisService redisService, String key, T value, Cache<String,Object> cache,final Long timeout,final TimeUnit timeUnit) {

//      设置二级缓存
        redisService.setCacheObject(key,value,timeout,timeUnit);
        log.info("更新redis缓存信息"+key);

//      设置一级缓存
        cache.put(key,value);
        log.info("更新本地缓存信息"+key);
    }
}
