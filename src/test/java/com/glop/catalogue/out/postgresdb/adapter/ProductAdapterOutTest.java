package com.glop.catalogue.out.postgresdb.adapter;

import com.glop.catalogue.domain.model.shop.ProductModel;
import com.glop.catalogue.out.postgresdb.entity.ProductEntity;
import com.glop.catalogue.out.postgresdb.mapper.ShopOutMapperImpl;
import com.glop.catalogue.out.postgresdb.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductAdapterOutTest {
    @Mock
    private ProductRepository productRepository;

    @Test
    void testFindByShopIdShouldReturnProductsOfShop() {
        UUID shopId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        ProductEntity product = new ProductEntity();
        product.setProductId(productId);
        product.setName("Croissant");
        product.setQuantity(0);
        product.setPrice(new BigDecimal("1.20"));
        product.setEarnedPoints(3);
        when(productRepository.findByShopShopId(shopId)).thenReturn(List.of(product));
        ProductAdapterOut adapter = new ProductAdapterOut(productRepository, new ShopOutMapperImpl());

        List<ProductModel> result = adapter.findByShopId(shopId);

        assertThat(result).containsExactly(new ProductModel(productId, "Croissant", 0, new BigDecimal("1.20"), null, 3));
    }
}
