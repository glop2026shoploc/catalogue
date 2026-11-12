package com.glop.catalogue.in.restapi.payload.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Schema(name = "ShopCreation", description = "Commerce à créer")
public class ShopCreationPayload {
    @NotBlank
    @Size(max = 150)
    @Schema(description = "Raison sociale", example = "MonCommercant", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull
    @Pattern(regexp = "^[0-9]{14}$")
    @Schema(description = "Numéro SIRET (14 chiffres)", example = "12345678900013", requiredMode = Schema.RequiredMode.REQUIRED)
    private String siret;

    @Schema(description = "Catégories du commerce ; les identifiants inconnus sont ignorés")
    private List<UUID> categoryIds = new ArrayList<>();

    @NotNull
    @Valid
    private ShopAddressPayload address;
}
