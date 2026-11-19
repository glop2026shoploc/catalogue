package com.glop.catalogue.in.restapi.controller;

import com.glop.catalogue.domain.port.in.ProductPortIn;
import com.glop.catalogue.in.restapi.mapper.ShopInMapper;
import com.glop.catalogue.in.restapi.payload.shop.ProductPayload;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductPortIn productPortIn;
    private final ShopInMapper shopInMapper;

    @GetMapping("/shops/{shopId}/products")
    @Operation(summary = "Liste les produits vendus par un commerce")
    public List<ProductPayload> listProducts(@PathVariable UUID shopId) {
        return shopInMapper.toProductPayloads(productPortIn.listProducts(shopId));
    }
}
