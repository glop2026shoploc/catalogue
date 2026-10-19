package com.glop.catalogue.out.postgresdb.adapter;

import com.glop.catalogue.domain.adapter.out.TestAdapterOut;
import com.glop.catalogue.domain.model.TestModel;
import com.glop.catalogue.out.postgresdb.entity.TestEntity;
import com.glop.catalogue.out.postgresdb.mapper.TestOutMapper;
import com.glop.catalogue.out.postgresdb.repository.TestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TestAdapterOutImpl implements TestAdapterOut {
    TestRepository testRepository;
    TestOutMapper mapper;
    @Override
    public TestModel findTestInformation() {
        return mapper.toTestModel(testRepository.findFirstByOrderByTestIdAsc().orElseThrow(/*TODO : create domainException*/));
    }

    @Override
    public void incrementRequestNumber() {
        TestEntity testEntity = testRepository.findFirstByOrderByTestIdAsc().orElseThrow(/*TODO : create domainException*/);
        testEntity.setTotalRequest(testEntity.getTotalRequest()+1);
        testRepository.save(testEntity);
    }

    @Override
    public TestModel updateMessage(String message) {
        TestEntity testEntity = testRepository.findFirstByOrderByTestIdAsc().orElseThrow();
        testEntity.setMessage(message);
        testRepository.save(testEntity);
        return mapper.toTestModel(testEntity);
    }
}
