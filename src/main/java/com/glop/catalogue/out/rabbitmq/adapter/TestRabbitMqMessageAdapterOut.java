package com.glop.catalogue.out.rabbitmq.adapter;

import com.glop.catalogue.domain.adapter.out.TestMessageAdapterOut;
import com.glop.catalogue.domain.model.QueueMessageModel;
import com.glop.catalogue.out.rabbitmq.mapper.QueueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestRabbitMqMessageAdapterOut implements TestMessageAdapterOut {
    private final RabbitTemplate rabbitTemplate;
    private final QueueMapper queueMapper;
    @Override
    public void sendMessage(QueueMessageModel queueMessageSend) {
        rabbitTemplate.convertAndSend("queueTest1",queueMapper.toQueueMessage(queueMessageSend));
    }
}
