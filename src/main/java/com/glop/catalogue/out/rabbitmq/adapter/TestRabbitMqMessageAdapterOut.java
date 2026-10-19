package com.glop.catalogue.out.rabbitmq.adapter;

import com.glop.catalogue.domain.adapter.out.TestMessageAdapterOut;
import com.glop.catalogue.domain.model.QueueMessageModel;
import com.glop.catalogue.out.rabbitmq.mapper.QueueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.glop.catalogue.out.rabbitmq.config.QueueConfig.QUEUE_TEST_1;
import static com.glop.catalogue.out.rabbitmq.config.QueueConfig.QUEUE_TEST_2;

@Component
@RequiredArgsConstructor
public class TestRabbitMqMessageAdapterOut implements TestMessageAdapterOut {
    private final RabbitTemplate rabbitTemplate;
    private final QueueMapper queueMapper;


    @Override
    public void sendMessageUser(QueueMessageModel queueMessageSend) {
        rabbitTemplate.convertAndSend(QUEUE_TEST_1,queueMapper.toQueueMessage(queueMessageSend));
    }
    @Override
    public void sendMessageInternal(QueueMessageModel queueMessageSend) {
        rabbitTemplate.convertAndSend(QUEUE_TEST_2,queueMapper.toQueueMessage(queueMessageSend));
    }
}
