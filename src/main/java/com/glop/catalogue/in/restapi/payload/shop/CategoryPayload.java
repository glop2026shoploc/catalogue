package com.glop.catalogue.in.restapi.payload.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(name = "Category", description = "Catégorie de commerce")
public class CategoryPayload {
    private UUID categoryId;
    @Schema(example = "Café")
    private String name;
}
