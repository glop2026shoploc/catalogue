package com.glop.catalogue.domain.port.out;

import com.glop.catalogue.domain.model.shop.OpeningHoursModel;
import com.glop.catalogue.domain.model.shop.ShopModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShopPortOut {
    boolean existsBySiret(String siret);

    /** Enregistre le magasin, son adresse et ses catégories. */
    ShopModel create(ShopModel shop);

    void addToFavorite(UUID shopId, UUID clientId);

    boolean isInFavorite(UUID shopId, UUID clientId);

    boolean exists(UUID shopId);

    ShopModel findById(UUID shopId);

    List<OpeningHoursModel> findOpeningHours(UUID shopId);

    void replaceOpeningHours(UUID shopId, List<OpeningHoursModel> openingHours);

    List<UUID> findFavoriteClientIds(UUID shopId);

}
