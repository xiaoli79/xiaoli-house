package org.xiaoli.xiaolimstemplateservice.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;

@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {

    @GetMapping("/info")
    public  void info(){
        log.info("接口调用测试");
    }


    @GetMapping("/result")
    public R<Void> result(int id){

        if(id < 0){
            return R.fail();
        }
        return R.ok();
    }

    @GetMapping("/exception")
    public R<Void> Exception (int id){

        if(id < 0){
            throw new ServiceException(ResultCode.INVALID_PARA);
        }
        if(id == 1){
            throw new ServiceException("id不能为1");

        }
        throw  new ServiceException("无效的参数",ResultCode.ERROR.getCode());

    }


}
