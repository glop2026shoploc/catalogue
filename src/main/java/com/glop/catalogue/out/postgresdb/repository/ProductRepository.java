package com.glop.catalogue.out.postgresdb.repository;

import com.glop.catalogue.out.postgresdb.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    List<ProductEntity> findByShopShopId(UUID shopId);
}
