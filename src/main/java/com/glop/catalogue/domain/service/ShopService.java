package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.config.exception.ShopAlreadyExistsException;
import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.domain.port.in.ShopPortIn;
import com.glop.catalogue.domain.port.out.CategoryPortOut;
import com.glop.catalogue.domain.port.out.ShopPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
