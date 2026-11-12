package com.glop.catalogue.out.postgresdb.entity;

import com.glop.catalogue.domain.model.shop.ShopStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "shop")
@Getter
@Setter
public class ShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "shop_id")
    private UUID shopId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /** char(14) en base, 14 chiffres (contrainte CHECK côté SQL). */
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "siret_number", nullable = false, unique = true, length = 14)
    private String siretNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ShopStatus status;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id", nullable = false, unique = true)
    private AddressEntity address;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "shop_category",
            joinColumns = @JoinColumn(name = "shop_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<CategoryEntity> categories = new HashSet<>();
}
