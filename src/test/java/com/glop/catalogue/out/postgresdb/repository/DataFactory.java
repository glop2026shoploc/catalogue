package com.glop.catalogue.out.postgresdb.repository;

import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.out.postgresdb.entity.AddressEntity;
import com.glop.catalogue.out.postgresdb.entity.ClientShopFavoriteEntity;
import com.glop.catalogue.out.postgresdb.entity.ShopEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;
@Component
public class DataFactory {
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ClientShopFavoriteRepository clientShopFavoriteRepository;

    private Random random = new Random();
    public ShopEntity createShopEntity(){
        AddressEntity address = new AddressEntity();
        address.setRoad("place du Général de Gaulle");
        address.setCityId(UUID.randomUUID());
        ShopEntity shop = new ShopEntity();
        shop.setName("Café du Marché");
        shop.setSiretNumber(String.format("%014d", random.nextLong(100_000_000_000_000L)));
        shop.setStatus(ShopStatus.VALIDATED);
        shop.setAddress(address);
        return shopRepository.save(shop);
    }
}
