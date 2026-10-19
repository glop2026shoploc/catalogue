package com.glop.catalogue.in.restapi.controller;

import com.glop.catalogue.domain.adapter.in.TestAdapterIn;
import com.glop.catalogue.domain.adapter.in.TestQueueAdapterIn;
import com.glop.catalogue.in.restapi.mapper.QueueInMapper;
import com.glop.catalogue.domain.port.in.TestPortIn;
import com.glop.catalogue.in.restapi.mapper.TestInMapper;
import com.glop.catalogue.in.restapi.payload.QueueMessageSend;
import com.glop.catalogue.in.restapi.payload.TestPayload;
import com.glop.catalogue.in.restapi.payload.UpdateTestMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.glop.catalogue.in.restapi.config.error.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/test")
public class TestController {
    TestPortIn testAdapterIn;
    TestInMapper mapper;
    QueueInMapper queueInMapper;
    TestQueueAdapterIn queueAdapterIn;
    @GetMapping
    @Operation(summary = "Get test information")
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Information successfully found",
            content = @Content(schema = @Schema(implementation = TestPayload.class))),
            @ApiResponse(responseCode = "400", description = "There is a problem in your request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Something went wrong...",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
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
