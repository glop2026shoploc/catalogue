package com.glop.catalogue.domain.port.out;

import com.glop.catalogue.domain.model.shop.ProductModel;

import java.util.List;
import java.util.UUID;

public interface ProductPortOut {
    List<ProductModel> findByShopId(UUID shopId);
}
