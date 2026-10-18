package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.adapter.in.TestQueueAdapterIn;
import com.glop.catalogue.domain.adapter.out.TestMessageAdapterOut;
import com.glop.catalogue.domain.model.QueueMessageModel;
import com.glop.catalogue.in.restapi.payload.QueueMessageSend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestQueueService implements TestQueueAdapterIn {
    private final TestMessageAdapterOut messageAdapterOut;
    @Override
    public void sendMessage(QueueMessageModel queueMessageSend) {
        messageAdapterOut.sendMessageInternal(queueMessageSend);
        messageAdapterOut.sendMessageUser(queueMessageSend);
    }

    @Override
    public void printMessageInConsole(QueueMessageModel queueMessageSend) {
        System.out.println(queueMessageSend.getMessage());
    }
}
