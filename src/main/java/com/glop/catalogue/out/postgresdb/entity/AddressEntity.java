package com.glop.catalogue.out.postgresdb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
public class AddressEntity {
    @Id
    private UUID addressId;

    private String road;
    private String number;
    private String complement;
    private UUID cityId;
}
