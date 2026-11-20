package com.glop.catalogue.domain.port.in;

import com.glop.catalogue.domain.model.shop.OpeningHoursModel;
import com.glop.catalogue.domain.model.shop.ShopModel;

import java.util.List;
import java.util.UUID;

public interface ShopPortIn {
    /** Crée un commerce en attente de validation */
    ShopModel create(ShopModel shop);

    void addClientFavoriteShop(UUID shopId, UUID clientId);

    void updateOpeningHours(UUID shopId, List<OpeningHoursModel> openingHours);
}
