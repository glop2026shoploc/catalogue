package com.glop.catalogue.out.rabbitmq.config;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class QueueConfig {
    public static final String QUEUE_TEST_1 = "queueTest1";
    public static final String QUEUE_TEST_2 = "queueTest2";
    @Bean
    public Queue queueTest1(){
        return new Queue(QUEUE_TEST_1);
    }
    @Bean
    public Queue queueTest2(){
        return new Queue(QUEUE_TEST_2);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
