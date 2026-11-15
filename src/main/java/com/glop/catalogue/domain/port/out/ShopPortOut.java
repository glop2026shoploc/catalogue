package com.glop.catalogue.domain.port.out;

import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.model.shop.ShopStatus;

import java.util.List;
import java.util.UUID;

public interface ShopPortOut {
    boolean existsBySiret(String siret);

    /** Enregistre le magasin, son adresse et ses catégories. */
    ShopModel create(ShopModel shop);

    List<ShopModel> findByStatus(ShopStatus status);

    ShopModel findById(UUID shopId);

    ShopModel updateStatus(UUID shopId, ShopStatus status);
}
