package com.glop.catalogue.out.postgresdb.repository;

import com.glop.catalogue.out.postgresdb.entity.ProductTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTypeRepository extends JpaRepository<ProductTypeEntity, String> {
}
