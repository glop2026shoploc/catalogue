package com.glop.catalogue.out.postgresdb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "client_shop_favorite",
        uniqueConstraints = @UniqueConstraint(columnNames = {"shop_id", "client_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientShopFavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "client_shop_favorite_id")
    private UUID clientShopFavoriteId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopEntity shop;

    /** Client du service users (autre base : pas de FK). */
    @Column(name = "client_id", nullable = false)
    private UUID clientId;
}