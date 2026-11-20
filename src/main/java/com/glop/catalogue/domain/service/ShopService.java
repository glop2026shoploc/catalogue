package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.config.exception.ShopAlreadyExistsException;
import com.glop.catalogue.domain.config.exception.ShopNotFoundException;
import com.glop.catalogue.domain.model.shop.OpeningHoursModel;
import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.domain.port.in.ShopPortIn;
import com.glop.catalogue.domain.port.out.CategoryPortOut;
import com.glop.catalogue.domain.port.out.NotificationPortOut;
import com.glop.catalogue.domain.port.out.ShopPortOut;
import com.glop.catalogue.util.OpeningHoursNotValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopService implements ShopPortIn {
    private final ShopPortOut shopPortOut;
    private final CategoryPortOut categoryPortOut;
    private final NotificationPortOut notificationPortOut;

    @Override
    public ShopModel create(ShopModel shop) {
        if (shopPortOut.existsBySiret(shop.getSiret())) {
            throw new ShopAlreadyExistsException("Un commerce existe déjà avec ce SIRET.");
        }
        List<UUID> categoryIds = shop.getCategoryIds() == null || shop.getCategoryIds().isEmpty()
                ? List.of()
                : categoryPortOut.findExistingIds(shop.getCategoryIds());
        shop.setCategoryIds(categoryIds);
        shop.setStatus(ShopStatus.WAITING);
        return shopPortOut.create(shop);
    }

    @Override
    public void addClientFavoriteShop(UUID shopId, UUID clientId) {
        if (!shopPortOut.exists(shopId))throw new ShopNotFoundException();
        if(shopPortOut.isInFavorite(shopId,clientId))return;
        shopPortOut.addToFavorite(shopId,clientId);

    }

    @Override
    public void updateOpeningHours(UUID shopId, List<OpeningHoursModel> openingHours) {
        ShopModel shop = shopPortOut.findById(shopId);
        List<OpeningHoursModel> newHours = openingHours == null ? List.of() : openingHours;
        checkOpeningHours(newHours);

        List<OpeningHoursModel> oldHours = shopPortOut.findOpeningHours(shopId);
        if (new HashSet<>(oldHours).equals(new HashSet<>(newHours))) {
            return; // aucun changement : pas de mise à jour, pas de notification
        }

        shopPortOut.replaceOpeningHours(shopId, newHours);

        List<UUID> favoriteClientIds = shopPortOut.findFavoriteClientIds(shopId);
        if (!favoriteClientIds.isEmpty()) {
            notificationPortOut.notifyOpeningHoursUpdated(shop, favoriteClientIds);
        }
    }

    private void checkOpeningHours(List<OpeningHoursModel> hours) {
        List<OpeningHoursModel> sorted = new ArrayList<>(hours);
        sorted.sort(Comparator.comparing(OpeningHoursModel::getDayOfWeek)
                .thenComparing(OpeningHoursModel::getOpenTime));

        OpeningHoursModel previous = null;
        for (OpeningHoursModel current : sorted) {
            if (!current.getCloseTime().isAfter(current.getOpenTime())) {
                throw new OpeningHoursNotValidException("L'heure de fermeture doit être après l'heure d'ouverture (jour "
                        + current.getDayOfWeek() + ").");
            }
            if (previous != null
                    && previous.getDayOfWeek().equals(current.getDayOfWeek())
                    && current.getOpenTime().isBefore(previous.getCloseTime())) {
                throw new OpeningHoursNotValidException("Deux créneaux se chevauchent le jour " + current.getDayOfWeek() + ".");
            }
            previous = current;
        }
    }

}
