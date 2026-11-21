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

    @GetMapping("/{shopId}")
    @Operation(summary = "Donne la fiche d'un commerce validé")
    public ShopPayload getShop(@PathVariable UUID shopId) {
        return shopInMapper.toShopPayload(shopPortIn.getValidatedShop(shopId));
    }

    /** GET /shops?cityId=(la ville de l'utilisateur) */
    @GetMapping
    @Operation(summary = "Liste les commerces validés d'une ville, triés par nom")
    public List<ShopPayload> listByCity(@RequestParam UUID cityId) {
        return shopInMapper.toShopPayloads(shopPortIn.listValidatedByCity(cityId));
    }
}
