package com.glop.catalogue.domain.port.out;

import com.glop.catalogue.domain.model.shop.ShopModel;

import java.util.List;
import java.util.UUID;

public interface NotificationPortOut {
    void notifyOpeningHoursUpdated(ShopModel shop, List<UUID> clientIds);
}
