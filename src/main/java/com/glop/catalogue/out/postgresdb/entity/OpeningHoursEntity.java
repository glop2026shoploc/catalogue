package com.glop.catalogue.out.postgresdb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "opening_hours")
@Getter
@Setter
public class OpeningHoursEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "opening_hours_id")
    private UUID openingHoursId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopEntity shop;

    /** 1 = lundi, 7 = dimanche (ISO). */
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;
}
