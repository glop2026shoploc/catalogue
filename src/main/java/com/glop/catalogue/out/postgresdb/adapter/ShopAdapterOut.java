package com.glop.catalogue.out.postgresdb.adapter;

import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.port.out.ShopPortOut;
import com.glop.catalogue.out.postgresdb.entity.ShopEntity;
import com.glop.catalogue.out.postgresdb.mapper.ShopOutMapper;
import com.glop.catalogue.out.postgresdb.repository.CategoryRepository;
import com.glop.catalogue.out.postgresdb.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import com.glop.catalogue.domain.model.shop.ShopStatus;

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
        entity.setCategory(categoryRepository.getReferenceById(shop.getCategory().getCategoryId()));
        return shopOutMapper.toShopModel(shopRepository.saveAndFlush(entity));
    }

    @Override
    public List<ShopModel> findAllValidated() {
        return shopOutMapper.toShopModels(shopRepository.findByStatusOrderByNameAsc(ShopStatus.VALIDATED));
    }

    @Override
    public List<ShopModel> findValidatedByCategoryId(UUID categoryId) {
        return shopOutMapper.toShopModels(shopRepository.findByStatusAndCategory_CategoryIdOrderByNameAsc(
                ShopStatus.VALIDATED, categoryId));
    }
}
