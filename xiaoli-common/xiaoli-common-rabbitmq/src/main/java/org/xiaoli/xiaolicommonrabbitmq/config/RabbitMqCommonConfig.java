package org.xiaoli.xiaolicommonrabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqCommonConfig {



    @Bean("testQueue")
    public Queue testQueue() {
        return QueueBuilder.durable("testQueue").build();
    }

    @Bean
    public MessageConverter jsonToMapMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
