package com.glop.catalogue.domain.adapter.out;

import com.glop.catalogue.domain.model.TestModel;

public interface TestAdapterOut {
    TestModel findTestInformation();

    void incrementRequestNumber();

    TestModel updateMessage(String message);
}
