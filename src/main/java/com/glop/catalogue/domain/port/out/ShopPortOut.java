package com.glop.catalogue.domain.port.out;

import com.glop.catalogue.domain.model.shop.ShopModel;

public interface ShopPortOut {
    boolean existsBySiret(String siret);

    /** Enregistre le magasin, son adresse et ses catégories. */
    ShopModel create(ShopModel shop);
}
