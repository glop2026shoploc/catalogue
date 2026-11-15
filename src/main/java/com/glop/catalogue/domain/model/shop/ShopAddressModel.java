package com.glop.catalogue.domain.model.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopAddressModel {
    private UUID addressId;
    private String road;
    private String number;
    private String complement;
    /** Ville du service users. */
    private UUID cityId;
}
