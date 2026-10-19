package com.glop.catalogue.in.restapi.controller;

import com.glop.catalogue.domain.adapter.in.TestAdapterIn;
import com.glop.catalogue.domain.adapter.in.TestQueueAdapterIn;
import com.glop.catalogue.in.restapi.mapper.QueueInMapper;
import com.glop.catalogue.in.restapi.mapper.TestInMapper;
import com.glop.catalogue.in.restapi.payload.QueueMessageSend;
import com.glop.catalogue.in.restapi.payload.TestPayload;
import com.glop.catalogue.in.restapi.payload.UpdateTestMessage;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/test")
public class TestController {
    TestAdapterIn testAdapterIn;
    TestInMapper mapper;
    QueueInMapper queueInMapper;
    TestQueueAdapterIn queueAdapterIn;
    @GetMapping
    public TestPayload getInformation(){
        return mapper.toTestPayload(testAdapterIn.getTestInformation(false));
    }

    @PostMapping
    public TestPayload updateInformation(@RequestBody UpdateTestMessage request) {
        return mapper.toTestPayload(testAdapterIn.updateMessage(request.getMessage()));
    }
    @PostMapping("/sendMessage")
    public void sendQueueOneMessage(@RequestBody QueueMessageSend messageSend){
        queueAdapterIn.sendMessage(queueInMapper.toQueueMessageModel(messageSend));
    }
}
