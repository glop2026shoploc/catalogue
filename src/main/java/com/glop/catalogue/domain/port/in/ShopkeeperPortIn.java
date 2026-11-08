package com.glop.catalogue.domain.port.in;

import com.glop.catalogue.domain.model.ShopkeeperModel;
import java.util.List;

public interface ShopkeeperPortIn {
    List<ShopkeeperModel> findShopkeepers(String category);
}
