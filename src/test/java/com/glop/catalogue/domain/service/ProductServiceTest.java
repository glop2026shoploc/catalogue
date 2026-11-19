package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.model.shop.ProductModel;
import com.glop.catalogue.domain.port.out.ProductPortOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductPortOut productPortOut;
    @InjectMocks
    private ProductService productService;

    @Test
    void testListProductsShouldReturnProductsFromPort() {
        UUID shopId = UUID.randomUUID();
        List<ProductModel> products = List.of(new ProductModel(UUID.randomUUID(), "Café allongé", 40, new BigDecimal("2.50"), 25, 0));
        when(productPortOut.findByShopId(shopId)).thenReturn(products);

        assertThat(productService.listProducts(shopId)).isEqualTo(products);
    }
}
