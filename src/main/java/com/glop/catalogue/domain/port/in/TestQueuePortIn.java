package com.glop.catalogue.domain.port.in;

import com.glop.catalogue.domain.model.QueueMessageModel;

public interface TestQueuePortIn {
    void sendMessage(QueueMessageModel queueMessageSend);
    void printMessageInConsole(QueueMessageModel queueMessageSend);
}
