package org.xiaoli.xiaolimstemplateservice.rabbit;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.xiaoli.xiaolimstemplateservice.domain.MessageDTO;

@Component
public class Consumer {



    @RabbitListener(queues = "testQueue")
    public void listenerQueue(MessageDTO messageDTO) {
        System.out.println("收到消息为"+messageDTO);
        System.out.println(MessageDTO.class);

    }

}
