package com.glop.catalogue.domain.port.in;

import com.glop.catalogue.domain.model.TestModel;

public interface TestPortIn {
    TestModel getTestInformation(Boolean shouldThrowError);

}
