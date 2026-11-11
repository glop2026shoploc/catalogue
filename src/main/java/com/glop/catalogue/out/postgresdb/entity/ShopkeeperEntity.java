package com.glop.catalogue.out.postgresdb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "shop")
@Getter
@Setter
@NoArgsConstructor
public class ShopkeeperEntity {
    @Id
    private UUID shopId;

    private String name;
    private String siretNumber;
    private String status;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false, unique = true)
    private AddressEntity address;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;
}
