package com.glop.catalogue.out.notification;

import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.port.out.NotificationPortOut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class MockNotificationAdapterOut implements NotificationPortOut {

    @Override
    public void notifyOpeningHoursUpdated(ShopModel shop, List<UUID> clientIds) {
        for (UUID clientId : clientIds) {
            log.info("[MOCK] e-mail au client {} : votre commerçant {} a changé ses horaires.", clientId, shop.getName());
        }
    }
}
