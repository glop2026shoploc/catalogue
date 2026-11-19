package com.glop.catalogue.out.postgresdb.adapter;

import com.glop.catalogue.domain.model.shop.ProductModel;
import com.glop.catalogue.domain.port.out.ProductPortOut;
import com.glop.catalogue.out.postgresdb.mapper.ShopOutMapper;
import com.glop.catalogue.out.postgresdb.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductAdapterOut implements ProductPortOut {
    private final ProductRepository productRepository;
    private final ShopOutMapper shopOutMapper;

    @Override
    public List<ProductModel> findByShopId(UUID shopId) {
        return shopOutMapper.toProductModels(productRepository.findByShopShopId(shopId));
    }
}
