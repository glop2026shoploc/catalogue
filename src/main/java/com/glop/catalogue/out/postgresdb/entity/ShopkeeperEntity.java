package com.glop.catalogue.out.postgresdb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "shopkeepers")
@Getter
@Setter
@NoArgsConstructor
public class ShopkeeperEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String category;
    private String address;
}
