package com.glop.catalogue.domain.model.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {
    private UUID productId;
    private String name;
    private Integer quantity;
    private BigDecimal price;
    private Integer pointsPrice;
    private Integer earnedPoints;
}
