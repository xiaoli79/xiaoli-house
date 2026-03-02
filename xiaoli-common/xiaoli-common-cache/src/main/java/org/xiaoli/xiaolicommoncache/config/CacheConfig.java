package org.xiaoli.xiaolicommoncache.config;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;



//对本地缓存实现动态配置~~
@Configuration
public class CacheConfig {


    /**
     * 初始容量
     */
    @Value("${caffeine.build.initial-capacity}")
    private Integer initialCapacity;

    /**
     * 最大容量
     */
    @Value("${caffeine.build.maximum-size}")
    private Long maximumSize;

    /**
     * 过期时间
     */
    @Value("${caffeine.build.expire}")
    private Long expire;


//  todo 配置动态调整
//  创建Cache对象
    @Bean
    public Cache<String,Object> cache(){
        return Caffeine.newBuilder()
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .expireAfterWrite(expire, TimeUnit.SECONDS)
                .build();
    }

}
