package com.glop.catalogue.domain.adapter.in;

import com.glop.catalogue.domain.model.TestModel;

public interface TestAdapterIn {
    TestModel getTestInformation(Boolean shouldThrowError);

    TestModel updateMessage(String message);
}
