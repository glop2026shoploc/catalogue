package com.glop.catalogue.in.restapi.controller;

import com.glop.catalogue.domain.adapter.in.TestAdapterIn;
import com.glop.catalogue.in.restapi.mapper.TestInMapper;
import com.glop.catalogue.in.restapi.payload.TestPayload;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/test")
public class TestController {
    TestAdapterIn testAdapterIn;
    TestInMapper mapper;
    @GetMapping
    public TestPayload getInformation(){
        return mapper.toTestPayload(testAdapterIn.getTestInformation(false));
    }
}
