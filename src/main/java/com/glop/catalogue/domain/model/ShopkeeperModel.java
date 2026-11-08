package com.glop.catalogue.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopkeeperModel {
    private String id;
    private String name;
    private String category;
    private String address;
}
