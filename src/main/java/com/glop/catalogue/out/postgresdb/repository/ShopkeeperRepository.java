package com.glop.catalogue.out.postgresdb.repository;

import com.glop.catalogue.out.postgresdb.entity.ShopkeeperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ShopkeeperRepository extends JpaRepository<ShopkeeperEntity, UUID> {
    List<ShopkeeperEntity> findAllByOrderByNameAsc();
    List<ShopkeeperEntity> findByCategoryIgnoreCaseOrderByNameAsc(String category);
}
