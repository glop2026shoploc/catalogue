package com.glop.catalogue.domain.port.in;

import com.glop.catalogue.domain.model.shop.ShopModel;

import java.util.List;

public interface ShopPortIn {
    /** Crée un commerce en attente de validation */
    ShopModel create(ShopModel shop);

    List<ShopModel> listWaiting();
}
