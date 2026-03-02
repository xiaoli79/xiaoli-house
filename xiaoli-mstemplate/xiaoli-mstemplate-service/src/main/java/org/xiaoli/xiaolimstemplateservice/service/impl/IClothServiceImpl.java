package org.xiaoli.xiaolimstemplateservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoli.xiaolicommoncache.utils.CacheUtil;
import org.xiaoli.xiaolicommondomain.constants.CacheConstants;
import org.xiaoli.xiaolicommonredis.service.RedisService;
import org.xiaoli.xiaolimstemplateservice.service.IClothService;

import java.util.concurrent.TimeUnit;


@Service
public class IClothServiceImpl implements IClothService {

    @Autowired
    private RedisService redisService;


    @Autowired
    private Cache<String, Object> cache;


    @Override
    public Integer clothPriceGet(Long proId) {

        String key = CacheConstants.CLOTH_key + proId;
//      这个方法中先从一级缓存中查询数据,若没查到,则从二级缓存查询数据,并且把二级缓存中查询到数据存储到一级缓存中~~
        Integer price = CacheUtil.getL2Cache(redisService, key, new TypeReference<Integer>() {
        }, cache);
        if (price != null) {
                return price;
        }
//      若price为null,则说明一级缓存和二级缓存中都没有数据,必须得从数据库中查询
        price = getPriceFromDB(proId);
        return price;
    }

    private Integer getPriceFromDB(Long proId) {
        //通过sql中查出指定商品的平均价格
        Integer price = 100;
        String key = CacheConstants.CLOTH_key + proId;
//      把从db中查询的数据存储到二级缓存和一级缓存
        CacheUtil.setL2Cache(redisService,key,price,cache,120L, TimeUnit.SECONDS);
        return price;
    }
}
