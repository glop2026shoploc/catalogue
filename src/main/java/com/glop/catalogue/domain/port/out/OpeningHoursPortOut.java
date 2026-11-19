package com.glop.catalogue.domain.port.out;

import com.glop.catalogue.domain.model.shop.OpeningHoursModel;

import java.util.List;
import java.util.UUID;

public interface OpeningHoursPortOut {
    /** Créneaux triés par jour puis par heure d'ouverture */
    List<OpeningHoursModel> findByShopId(UUID shopId);
}
