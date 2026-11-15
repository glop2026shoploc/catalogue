package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.config.exception.ShopAlreadyExistsException;
import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.domain.port.in.ShopPortIn;
import com.glop.catalogue.domain.port.out.CategoryPortOut;
import com.glop.catalogue.domain.port.out.ShopPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopService implements ShopPortIn {
    private final ShopPortOut shopPortOut;
    private final CategoryPortOut categoryPortOut;

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
    public List<ShopModel> listWaiting() {
        return shopPortOut.findByStatus(ShopStatus.WAITING);
    }

    @Override
    public ShopModel updateStatus(UUID shopId, ShopStatus status) {
        if (status == ShopStatus.WAITING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le statut doit être VALIDATED ou REJECTED.");
        }
        ShopModel shop = shopPortOut.findById(shopId);
        if (shop == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Commerce introuvable.");
        }
        if (shop.getStatus() != ShopStatus.WAITING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce commerce a déjà été validé ou refusé.");
        }
        return shopPortOut.updateStatus(shopId, status);
    }
}
