package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.model.shop.ProductModel;
import com.glop.catalogue.domain.port.in.ProductPortIn;
import com.glop.catalogue.domain.port.out.ProductPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService implements ProductPortIn {
    private final ProductPortOut productPortOut;

    @Override
    public List<ProductModel> listProducts(UUID shopId) {
        return productPortOut.findByShopId(shopId);
    }
}
