package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.adapter.in.TestAdapterIn;
import com.glop.catalogue.domain.adapter.out.TestAdapterOut;
import com.glop.catalogue.domain.model.TestModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TestService implements TestAdapterIn {
    TestAdapterOut testAdapter;
    @Override
    public TestModel getTestInformation(Boolean shouldThrowError) {
        if(shouldThrowError) System.out.println("todo");//TODO
        testAdapter.incrementRequestNumber();
        TestModel testModel =  testAdapter.findTestInformation();
        testModel.setMyOtherMessage("Test Message");
        return testModel;
    }

    @Override
    public TestModel updateMessage(String message) {
        TestModel testModel = testAdapter.updateMessage(message);
        testModel.setMyOtherMessage("Test Message");
        return testModel;
    }}
