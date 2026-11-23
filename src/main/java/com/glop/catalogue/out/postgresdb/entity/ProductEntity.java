package com.glop.catalogue.out.postgresdb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@Setter
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** null : le produit ne peut pas être payé en points. */
    @Column(name = "points_price")
    private Integer pointsPrice;

    @Column(name = "earned_points", nullable = false)
    private Integer earnedPoints;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_type", nullable = false)
    private ProductTypeEntity productType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopEntity shop;
}
