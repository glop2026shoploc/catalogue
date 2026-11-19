package com.glop.catalogue.domain.port.in;

import com.glop.catalogue.domain.model.shop.ProductModel;

import java.util.List;
import java.util.UUID;

public interface ProductPortIn {
    List<ProductModel> listProducts(UUID shopId);
}
