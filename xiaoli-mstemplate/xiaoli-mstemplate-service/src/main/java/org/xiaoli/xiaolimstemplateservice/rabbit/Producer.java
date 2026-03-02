package org.xiaoli.xiaolimstemplateservice.rabbit;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xiaoli.xiaolimstemplateservice.domain.MessageDTO;



// 生产者~~~
@Component
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void producMsg(MessageDTO messageDTO){
        rabbitTemplate.convertAndSend("testQueue",messageDTO);
    }
}
