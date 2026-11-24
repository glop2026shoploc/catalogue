package com.glop.catalogue.out.postgresdb.repository;

import com.glop.catalogue.out.postgresdb.entity.ClientShopFavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;

public interface ClientShopFavoriteRepository extends JpaRepository<ClientShopFavoriteEntity, UUID> {
    
    boolean existsByClientIdAndShop_ShopId(UUID clientId, UUID shopId);
    long deleteByClientIdAndShop_ShopId(UUID clientId, UUID shopId);

    @Query("select f.shop.shopId from ClientShopFavoriteEntity f where f.clientId = :clientId")
    Set<UUID> findShopIdsByClientId(@Param("clientId") UUID clientId);
}
