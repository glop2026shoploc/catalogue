package com.glop.catalogue.out.postgresdb.repository;

import com.glop.catalogue.out.postgresdb.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface TestRepository extends JpaRepository<TestEntity, UUID> {

    Optional<TestEntity> findFirstByOrderByTestIdAsc();
}
