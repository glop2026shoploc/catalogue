package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.config.exception.ShopAlreadyExistsException;
import com.glop.catalogue.domain.config.exception.CategoryNotFoundException;
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
        if (shop.getCategory() == null || shop.getCategory().getCategoryId() == null
                || !categoryPortOut.existsById(shop.getCategory().getCategoryId())) {
            throw new CategoryNotFoundException("La catégorie demandée n'existe pas.");
        }
        shop.setStatus(ShopStatus.WAITING);
        return shopPortOut.create(shop);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShopModel> findValidatedShops(UUID categoryId) {
        if (categoryId == null) return shopPortOut.findAllValidated();
        return shopPortOut.findValidatedByCategoryId(categoryId);
    }
}
