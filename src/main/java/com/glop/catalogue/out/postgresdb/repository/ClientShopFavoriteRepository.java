package com.glop.catalogue.out.postgresdb.repository;

import com.glop.catalogue.out.postgresdb.entity.ClientShopFavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClientShopFavoriteRepository extends JpaRepository<ClientShopFavoriteEntity, UUID> {
    
    boolean existsByClientIdAndShop_ShopId(UUID clientId, UUID shopId);
    List<ClientShopFavoriteEntity> findAllByShop_ShopId(UUID shopId);
}
