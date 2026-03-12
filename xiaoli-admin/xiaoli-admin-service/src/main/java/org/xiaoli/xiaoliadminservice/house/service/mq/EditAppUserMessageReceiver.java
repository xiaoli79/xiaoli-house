package org.xiaoli.xiaoliadminservice.house.service.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xiaoli.xiaoliadminapi.appUser.domain.dto.AppUserDTO;
import org.xiaoli.xiaoliadminservice.house.service.IHouseService;
import org.xiaoli.xiaoliadminservice.user.config.RabbitConfig;
import org.xiaoli.xiaolicommoncore.utils.JsonUtil;

import java.util.List;


@Component
@Slf4j
@RabbitListener(bindings = {
        @QueueBinding(
                value = @Queue(),
                exchange = @Exchange(value = RabbitConfig.EXCHANGE_NAME,type = ExchangeTypes.FANOUT)
        )
})
public class EditAppUserMessageReceiver {


    @Autowired
    private IHouseService houseService;

    @RabbitHandler
    public void process(AppUserDTO appUserDTO){

        //1.获取用户下房源列表

        if(null == appUserDTO || null == appUserDTO.getUserId()){
            log.error("MQ接收到的用户修改消息为空或用户ID为空");
            return;
        }

        log.info("MQ成功接收到消息，message:{}", JsonUtil.obj2String(appUserDTO));

        try{
            //2.获取用户下的房源ID列表
            List<Long> houseIds = houseService.listByUserId(appUserDTO.getUserId());
            if(null == houseIds || houseIds.isEmpty()){
                log.warn("该用户下面没有房子：{}", JsonUtil.obj2String(appUserDTO));
                return;
            }

            //3.更新用户下全量房源的缓存
            for(Long houseId : houseIds){
                houseService.cacheHouse(houseId);
            }

        }catch (Exception e){
            log.error("处理用户更新时，更新房源缓存异常，appUserDTO:{}",JsonUtil.obj2String(appUserDTO),e);
        }
    }

}
