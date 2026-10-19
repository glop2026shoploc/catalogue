package com.glop.catalogue.in.rabbitmq.controller;

import com.glop.catalogue.domain.port.in.TestQueuePortIn;
import com.glop.catalogue.in.rabbitmq.mapper.RabbitMqMapper;
import com.glop.catalogue.transversal.model.QueueMessageTransversalModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMqListener {
    private final RabbitMqMapper rabbitMqMapper;
    private final TestQueuePortIn testQueueAdapterIn;
    @RabbitListener(queues = "queueTest1")
    public void receiveMessage(QueueMessageTransversalModel queueMessageTransversalModel){
        testQueueAdapterIn.printMessageInConsole(rabbitMqMapper.toQueueMessageModel(queueMessageTransversalModel));
    }
}
