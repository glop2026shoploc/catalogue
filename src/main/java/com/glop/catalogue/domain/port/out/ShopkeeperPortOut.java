package com.glop.catalogue.domain.port.out;

import com.glop.catalogue.domain.model.ShopkeeperModel;
import java.util.List;

public interface ShopkeeperPortOut {
    List<ShopkeeperModel> findAll();
    List<ShopkeeperModel> findByCategory(String category);
}
