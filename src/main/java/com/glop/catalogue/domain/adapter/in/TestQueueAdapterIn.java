package com.glop.catalogue.domain.adapter.in;

import com.glop.catalogue.domain.model.QueueMessageModel;
import com.glop.catalogue.in.restapi.payload.QueueMessageSend;

public interface TestQueueAdapterIn {
    void sendMessage(QueueMessageModel queueMessageSend);
    void printMessageInConsole(QueueMessageModel queueMessageSend);
}
