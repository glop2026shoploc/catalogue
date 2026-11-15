package com.glop.catalogue.out.postgresdb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id")
    private UUID addressId;

    @Column(name = "road", nullable = false)
    private String road;

    /** Texte et non nombre : "12 bis", "3B"... */
    @Column(name = "number", length = 20)
    private String number;

    @Column(name = "complement")
    private String complement;

    /** Ville du service users (autre base : pas de relation JPA). */
    @Column(name = "city_id", nullable = false)
    private UUID cityId;
}
