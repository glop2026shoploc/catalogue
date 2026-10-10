package com.glop.catalogue.out.postgresdb.mapper;

import com.glop.catalogue.domain.model.TestModel;
import com.glop.catalogue.out.postgresdb.entity.TestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class TestOutMapper {
    @Mapping(target = "numberOfRequest", source = "totalRequest")
    @Mapping(target = "myOtherMessage", ignore = true)
    @Mapping(target = "myMessage", source = "message")
    public abstract TestModel toTestModel(TestEntity entity);
}
