package com.glop.catalogue.in.restapi.controller;

import com.glop.catalogue.domain.port.in.ShopPortIn;
import com.glop.catalogue.in.restapi.mapper.ShopInMapper;
import com.glop.catalogue.in.restapi.payload.shop.ShopCreationPayload;
import com.glop.catalogue.in.restapi.payload.shop.ShopPayload;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ShopController {
    private final ShopPortIn shopPortIn;
    private final ShopInMapper shopInMapper;

    /** Called par le service users à l'inscription d'un commerçant. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crée un commerce en attente de validation")
    public ShopPayload create(@Valid @RequestBody ShopCreationPayload payload) {
        return shopInMapper.toShopPayload(shopPortIn.create(shopInMapper.toShopModel(payload)));
    }
}
