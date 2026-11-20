package com.glop.catalogue.in.restapi.controller;

import com.glop.catalogue.config.security.AuthenticatedUser;
import com.glop.catalogue.domain.port.in.ShopPortIn;
import com.glop.catalogue.in.restapi.mapper.ShopInMapper;
import com.glop.catalogue.in.restapi.payload.shop.OpeningHoursPayload;
import com.glop.catalogue.in.restapi.payload.shop.ShopCreationPayload;
import com.glop.catalogue.in.restapi.payload.shop.ShopPayload;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
/**
 * AI
 */
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

    @PostMapping("/{id}/favorite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Mettre un magasin en favoris")
    @PreAuthorize("hasRole('CLIENT')")
    public void addToFavorite(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable(name = "id") UUID shopId){
        shopPortIn.addClientFavoriteShop(shopId, user.clientId());
    }
    
    @PutMapping("/{id}/opening-hours")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Modifie les horaires d'un commerçant et notifie les clients qui ont le commerce en favori")
    @PreAuthorize("hasRole('MERCHANT', 'OWNER')")
    public void updateOpeningHours(@AuthenticationPrincipal AuthenticatedUser user, 
                                   @PathVariable(name= "id") UUID shopID,
                                   @RequestBody List<@Valid OpeningHoursPayload> payload
                                   ) {
        if (!shopId.equals(user.shopId())) {
            throw new AccessDeniedException("Ce commerce ne vous appartient pas");
        }
        shopPortIn.updateOpeningHours(shopId, shopInMapper.toOpeningHoursModels(payload));
    }
}
