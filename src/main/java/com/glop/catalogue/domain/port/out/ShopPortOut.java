package com.glop.catalogue.domain.port.out;

import com.glop.catalogue.domain.model.shop.ShopModel;
import java.util.List;
import java.util.UUID;

public interface ShopPortOut {
    boolean existsBySiret(String siret);

    /** Enregistre le magasin, son adresse et ses catégories. */
    ShopModel create(ShopModel shop);
    List<ShopModel> findAllValidated();
    List<ShopModel> findValidatedByCategoryId(UUID categoryId);
}
