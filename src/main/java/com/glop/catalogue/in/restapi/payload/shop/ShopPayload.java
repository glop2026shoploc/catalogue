package com.glop.catalogue.in.restapi.payload.shop;

import com.glop.catalogue.domain.model.shop.ShopStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Schema(name = "Shop", description = "Commerce")
public class ShopPayload {
    private UUID shopId;
    private String name;
    private String siret;
    private List<UUID> categoryIds;
    private ShopAddressPayload address;
    @Schema(description = "Statut de validation", example = "WAITING")
    private ShopStatus status;
}
