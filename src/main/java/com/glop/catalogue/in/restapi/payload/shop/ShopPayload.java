package com.glop.catalogue.in.restapi.payload.shop;

import com.glop.catalogue.domain.model.shop.ShopStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(name = "Shop", description = "Commerce")
public class ShopPayload {
    @NotNull
    @Schema(description = "Identifiant du commerce", example = "20700000-0000-4000-8000-000000000001", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID shopId;
    @NotNull
    @Schema(description = "Nom du commerce", example = "Boulangerie du Centre", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @NotNull
    @Schema(description = "Numéro SIRET", example = "12345678900013", requiredMode = Schema.RequiredMode.REQUIRED)
    private String siret;
    @NotNull
    @Schema(description = "Catégorie du commerce", requiredMode = Schema.RequiredMode.REQUIRED)
    private CategoryPayload category;
    @NotNull
    @Schema(description = "Adresse du commerce", requiredMode = Schema.RequiredMode.REQUIRED)
    private ShopAddressPayload address;
    @NotNull
    @Schema(description = "Statut de validation", example = "VALIDATED", requiredMode = Schema.RequiredMode.REQUIRED)
    private ShopStatus status;
}
