package com.glop.catalogue.out.postgresdb.adapter;

import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.port.out.ShopPortOut;
import com.glop.catalogue.out.postgresdb.entity.ShopEntity;
import com.glop.catalogue.out.postgresdb.mapper.ShopOutMapper;
import com.glop.catalogue.out.postgresdb.repository.CategoryRepository;
import com.glop.catalogue.out.postgresdb.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class ShopAdapterOut implements ShopPortOut {
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final ShopOutMapper shopOutMapper;

    @Override
    public boolean existsBySiret(String siret) {
        return shopRepository.existsBySiretNumber(siret);
    }

    @Override
    public ShopModel create(ShopModel shop) {
        ShopEntity entity = shopOutMapper.toShopEntity(shop);
        if (shop.getCategoryIds() != null && !shop.getCategoryIds().isEmpty()) {
            entity.setCategories(new HashSet<>(categoryRepository.findAllById(shop.getCategoryIds())));
        }
        return shopOutMapper.toShopModel(shopRepository.saveAndFlush(entity));
    }
}
