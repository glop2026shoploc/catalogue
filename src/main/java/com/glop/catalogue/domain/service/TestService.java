package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.config.exception.AskedErrorException;
import com.glop.catalogue.domain.port.in.TestPortIn;
import com.glop.catalogue.domain.port.out.TestPortOut;
import com.glop.catalogue.domain.model.TestModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TestService implements TestPortIn {
    TestPortOut testAdapter;
    @Override
    public TestModel getTestInformation(Boolean shouldThrowError) {
        if(shouldThrowError) throw new AskedErrorException("");
        testAdapter.incrementRequestNumber();
        TestModel testModel =  testAdapter.findTestInformation();
        testModel.setMyOtherMessage("Test Message");
        return testModel;
    }
}
