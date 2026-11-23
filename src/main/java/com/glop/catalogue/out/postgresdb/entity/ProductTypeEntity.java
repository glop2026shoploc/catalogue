package com.glop.catalogue.out.postgresdb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_type")
@Getter
@Setter
public class ProductTypeEntity {

    @Id
    @Column(name = "name", length = 50)
    private String name;
}
