package com.glop.catalogue.in.restapi.payload.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(name = "Category", description = "Catégorie de commerce")
public class CategoryPayload {
    @Schema(description = "Identifiant de la catégorie", example = "20700000-0000-4000-8000-000000000011", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID categoryId;
    @Schema(description = "Nom de la catégorie", example = "Boulangerie", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
