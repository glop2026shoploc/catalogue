package com.glop.catalogue.domain.port.in;

import com.glop.catalogue.domain.model.shop.ShopModel;

public interface ShopPortIn {
    /** Crée un commerce en attente de validation */
    ShopModel create(ShopModel shop);
}
