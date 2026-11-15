package com.glop.catalogue.in.restapi.controller;

import com.glop.catalogue.domain.port.in.CategoryPortIn;
import com.glop.catalogue.in.restapi.mapper.ShopInMapper;
import com.glop.catalogue.in.restapi.payload.shop.CategoryPayload;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryPortIn categoryPortIn;
    private final ShopInMapper shopInMapper;

    @GetMapping("/categories")
    @Operation(summary = "Liste les catégories de commerce, triées par nom")
    public List<CategoryPayload> listCategories() {
        return shopInMapper.toCategoryPayloads(categoryPortIn.listCategories());
    }
}
