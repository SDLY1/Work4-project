package com.example.wwork4.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConfig {
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public Queue getChatMessageQueue() {
        return new Queue("chat.message.queue", true, false, false);
    }
    @Bean
    public Queue getSaveChatMsgQueue() {
        return new Queue("save.chat.queue", true, false, false);
    }
}
