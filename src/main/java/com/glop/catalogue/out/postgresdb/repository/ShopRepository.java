package com.glop.catalogue.out.postgresdb.repository;

import com.glop.catalogue.out.postgresdb.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import com.glop.catalogue.domain.model.shop.ShopStatus;
import java.util.List;

import java.util.UUID;

public interface ShopRepository extends JpaRepository<ShopEntity, UUID> {
    boolean existsBySiretNumber(String siretNumber);

    @EntityGraph(attributePaths = {"address", "category"})
    List<ShopEntity> findByStatusOrderByNameAsc(ShopStatus status);

    @EntityGraph(attributePaths = {"address", "category"})
    List<ShopEntity> findByStatusAndCategory_CategoryIdOrderByNameAsc(ShopStatus status, UUID categoryId);
}
