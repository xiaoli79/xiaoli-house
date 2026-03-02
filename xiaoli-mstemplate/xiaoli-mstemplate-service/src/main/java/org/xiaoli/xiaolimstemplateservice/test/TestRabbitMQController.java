package org.xiaoli.xiaolimstemplateservice.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolimstemplateservice.domain.MessageDTO;
import org.xiaoli.xiaolimstemplateservice.rabbit.Producer;

@RestController
@RequestMapping("/test/mq")
public class TestRabbitMQController {

    @Autowired
    private Producer producer;

    @PostMapping("/send")
    public R<Void> send(){
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setType("系统");
        messageDTO.setMessage("请您尽快完成升级");
        producer.producMsg(messageDTO);
        return R.ok();
    }
}
