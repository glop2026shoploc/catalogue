package com.glop.catalogue.domain.model.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopModel {
    private UUID shopId;
    private String name;
    private String siret;
    @Builder.Default
    private List<UUID> categoryIds = new ArrayList<>();
    private ShopAddressModel address;
    private ShopStatus status;
}
