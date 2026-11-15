package com.glop.catalogue.in.restapi.controller;

import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.domain.port.in.ShopPortIn;
import com.glop.catalogue.in.restapi.mapper.ShopInMapper;
import com.glop.catalogue.in.restapi.payload.shop.ShopCreationPayload;
import com.glop.catalogue.in.restapi.payload.shop.ShopPayload;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/admin/waiting")
    @Operation(summary = "Liste les commerces en attente de validation")
    public List<ShopPayload> listWaiting() {
        return shopInMapper.toShopPayloads(shopPortIn.listWaiting());
    }

    @PatchMapping("/admin/{shopId}/status")
    @Operation(summary = "Accepte ou refuse un commerce en attente")
    public ShopPayload updateStatus(@PathVariable UUID shopId, @RequestParam ShopStatus status) {
        return shopInMapper.toShopPayload(shopPortIn.updateStatus(shopId, status));
    }
}
