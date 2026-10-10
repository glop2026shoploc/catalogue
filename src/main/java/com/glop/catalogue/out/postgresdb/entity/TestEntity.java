package com.glop.catalogue.out.postgresdb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestEntity {

    @Id
    @GeneratedValue
    private UUID testId;

    private String message;
    private Integer totalRequest;
}