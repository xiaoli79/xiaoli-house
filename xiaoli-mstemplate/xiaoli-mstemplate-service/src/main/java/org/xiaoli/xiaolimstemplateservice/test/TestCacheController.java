package org.xiaoli.xiaolimstemplateservice.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoli.xiaolicommoncache.utils.CacheUtil;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommonredis.service.RedisService;
import org.xiaoli.xiaolimstemplateservice.service.IClothService;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/test/cache")
public class TestCacheController {


    @Autowired
    private RedisService redisService;

    @Autowired
    private Cache<String, Object> cache;


    @Autowired
    private IClothService  clothService;



    @GetMapping("/get")
    public R<Void> get(){
        String key = "key";
        CacheUtil.getL2Cache(redisService, key, new TypeReference<List<String>>() {
        },cache);

        return R.ok();
    }


    @GetMapping("/cloth/get")
    public R<Integer> clothGet(Long proId){


        return R.ok(clothService.clothPriceGet(proId));




    }

}
