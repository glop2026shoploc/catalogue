package com.glop.catalogue.domain.port.out;

import com.glop.catalogue.domain.model.TestModel;

public interface TestPortOut {
    TestModel findTestInformation();

    void incrementRequestNumber();
}
