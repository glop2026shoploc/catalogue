package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.config.exception.ShopAlreadyExistsException;
import com.glop.catalogue.domain.config.exception.ShopNotFoundException;
import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.domain.port.in.ShopPortIn;
import com.glop.catalogue.domain.port.out.CategoryPortOut;
import com.glop.catalogue.domain.port.out.ShopPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
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
    @Transactional(readOnly = true)
    public List<ShopModel> listValidatedByCity(UUID cityId, UUID clientId) {
        List<ShopModel> shops = shopPortOut.findByStatusAndCity(ShopStatus.VALIDATED, cityId);
        if (clientId == null || shops.isEmpty()) {
            return shops;
        }
        Set<UUID> favoriteShopIds = shopPortOut.findFavoriteShopIds(clientId);
        shops.forEach(shop -> shop.setFavorite(favoriteShopIds.contains(shop.getShopId())));
        return shops;
    }

    @Override
    public void addOrRemoveClientFavoriteShop(UUID shopId, boolean isFavorite, UUID clientId) {
        if (!shopPortOut.exists(shopId))throw new ShopNotFoundException();
        if(isFavorite){
            shopPortOut.addToFavorite(shopId,clientId);
        }else{
            shopPortOut.removeFavorite(shopId,clientId);
        }



    }

}
