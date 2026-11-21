package com.glop.catalogue.domain.port.in;

import com.glop.catalogue.domain.model.shop.ShopModel;

import java.util.List;
import java.util.UUID;

public interface ShopPortIn {
    /** Crée un commerce en attente de validation */
    ShopModel create(ShopModel shop);

    ShopModel getValidatedShop(UUID shopId);

    /** Commerces validés de la ville donnée, triés par nom. */
    List<ShopModel> listValidatedByCity(UUID cityId);
}
