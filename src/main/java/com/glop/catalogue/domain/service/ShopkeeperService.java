package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.model.ShopkeeperModel;
import com.glop.catalogue.domain.port.in.ShopkeeperPortIn;
import com.glop.catalogue.domain.port.out.ShopkeeperPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopkeeperService implements ShopkeeperPortIn {
    private final ShopkeeperPortOut shopkeeperPortOut;

    @Override
    public List<ShopkeeperModel> findShopkeepers(String category) {
        if (category == null || category.isBlank()) {
            return shopkeeperPortOut.findAll();
        }
        return shopkeeperPortOut.findByCategory(category.trim());
    }
}
