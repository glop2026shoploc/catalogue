package com.glop.catalogue.out.postgresdb.adapter;

import com.glop.catalogue.domain.model.shop.OpeningHoursModel;
import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.port.out.ShopPortOut;
import com.glop.catalogue.out.postgresdb.entity.ClientShopFavoriteEntity;
import com.glop.catalogue.out.postgresdb.entity.OpeningHoursEntity;
import com.glop.catalogue.out.postgresdb.entity.ShopEntity;
import com.glop.catalogue.out.postgresdb.mapper.ShopOutMapper;
import com.glop.catalogue.out.postgresdb.repository.CategoryRepository;
import com.glop.catalogue.out.postgresdb.repository.ClientShopFavoriteRepository;
import com.glop.catalogue.out.postgresdb.repository.OpeningHoursRepository;
import com.glop.catalogue.out.postgresdb.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ShopAdapterOut implements ShopPortOut {
    private final ShopRepository shopRepository;
    private final ClientShopFavoriteRepository clientShopFavoriteRepository;
    private final CategoryRepository categoryRepository;
    private final ShopOutMapper shopOutMapper;
    private final OpeningHoursRepository openingHoursRepository;

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

    @Override
    public void addToFavorite(UUID shopId, UUID clientId) {
        ClientShopFavoriteEntity clientShopFavoriteEntity = new ClientShopFavoriteEntity();
        clientShopFavoriteEntity.setClientId(clientId);
        clientShopFavoriteEntity.setShop(shopRepository.findById(shopId).get());
        clientShopFavoriteRepository.save(clientShopFavoriteEntity);
    }

    @Override
    public boolean isInFavorite(UUID shopId, UUID clientId) {
        return clientShopFavoriteRepository.existsByClientIdAndShop_ShopId(clientId, shopId);
    }

    @Override
    public boolean exists(UUID shopId) {
        return shopRepository.existsById(shopId);
    }

    public ShopModel findById(UUID shopId) {
        return shopOutMapper.toShopModel(shopRepository.findById(shopId).get());
    }

    @Override
    public List<OpeningHoursModel> findOpeningHours(UUID shopId) {
        return shopOutMapper.toOpeningHoursModels(openingHoursRepository.findByShopShopId(shopId));
    }
    @Override
    public void replaceOpeningHours(UUID shopId, List<OpeningHoursModel> openingHours) {
        openingHoursRepository.deleteAll(openingHoursRepository.findByShopShopId(shopId));
        openingHoursRepository.flush();

        ShopEntity shop = shopRepository.findById(shopId).get();
        for (OpeningHoursModel model : openingHours) {
            OpeningHoursEntity entity = shopOutMapper.toOpeningHoursEntity(model);
            entity.setShop(shop);
            openingHoursRepository.save(entity);
        }
    }

    @Override
    public List<UUID> findFavoriteClientIds(UUID shopId) {
        List<UUID> clientIds = new ArrayList<>();
        for (ClientShopFavoriteEntity favorite : clientShopFavoriteRepository.findAllByShop_ShopId(shopId)) {
            clientIds.add(favorite.getClientId());
        }
        return clientIds;
    }
}