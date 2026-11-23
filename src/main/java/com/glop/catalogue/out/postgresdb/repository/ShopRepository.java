package com.glop.catalogue.out.postgresdb.repository;

import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.out.postgresdb.entity.ShopEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShopRepository extends JpaRepository<ShopEntity, UUID> {
    boolean existsBySiretNumber(String siretNumber);

    @EntityGraph(attributePaths = {"address", "categories"})
    List<ShopEntity> findByStatusAndAddressCityIdOrderByNameAsc(ShopStatus status, UUID cityId);
}
