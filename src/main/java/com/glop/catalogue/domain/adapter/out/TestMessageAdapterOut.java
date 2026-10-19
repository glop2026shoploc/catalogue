package com.glop.catalogue.domain.adapter.out;

import com.glop.catalogue.domain.model.QueueMessageModel;
import com.glop.catalogue.in.restapi.payload.QueueMessageSend;

public interface TestMessageAdapterOut {
    void sendMessageUser(QueueMessageModel queueMessageSend);
    void sendMessageInternal(QueueMessageModel queueMessageSend);
}
