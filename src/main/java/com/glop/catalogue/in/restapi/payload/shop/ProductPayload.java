package com.glop.catalogue.in.restapi.payload.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Schema(name = "Product", description = "Produit vendu par un commerce")
public class ProductPayload {
    private UUID productId;
    @Schema(example = "Café allongé")
    private String name;
    @Schema(description = "Quantité en stock", example = "40")
    private Integer quantity;
    @Schema(example = "2.50")
    private BigDecimal price;
    @Schema(description = "Prix en points, null si le produit ne peut pas être payé en points", example = "25")
    private Integer pointsPrice;
    @Schema(description = "Points gagnés à l'achat", example = "0")
    private Integer earnedPoints;
}
