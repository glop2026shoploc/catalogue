package com.glop.catalogue.in.restapi.payload.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(name = "ShopAddress", description = "Adresse postale d'un commerce")
public class ShopAddressPayload {
    @Schema(description = "Identifiant de l'adresse (renseigné en réponse)", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID addressId;

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Voie", example = "rue de la République", requiredMode = Schema.RequiredMode.REQUIRED)
    private String road;

    @Size(max = 20)
    @Schema(description = "Numéro dans la voie", example = "12 bis")
    private String number;

    @Size(max = 255)
    @Schema(description = "Complément d'adresse", example = "Bâtiment B")
    private String complement;

    @NotNull
    @Schema(description = "Identifiant de la ville (référentiel du service users)", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID cityId;
}
