package com.glop.catalogue.in.restapi.mapper;

import com.glop.catalogue.domain.model.TestModel;
import com.glop.catalogue.in.restapi.payload.TestPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class TestInMapper {

    @Mapping(target = "testMessage2", source = "myOtherMessage")
    @Mapping(target = "testMessage1", source = "myMessage")
    @Mapping(target = "numberOfTestRequestSinceBeginning", source = "numberOfRequest")
    public abstract TestPayload toTestPayload(TestModel model);
}
