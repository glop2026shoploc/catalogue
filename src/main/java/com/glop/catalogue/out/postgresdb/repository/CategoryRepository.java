package com.glop.catalogue.out.postgresdb.repository;

import com.glop.catalogue.out.postgresdb.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
}
