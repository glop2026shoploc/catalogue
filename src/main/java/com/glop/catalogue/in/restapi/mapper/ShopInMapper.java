package com.glop.catalogue.in.restapi.mapper;

import com.glop.catalogue.domain.model.shop.CategoryModel;
import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.in.restapi.payload.shop.CategoryPayload;
import com.glop.catalogue.in.restapi.payload.shop.ShopCreationPayload;
import com.glop.catalogue.in.restapi.payload.shop.ShopPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShopInMapper {

    @Mapping(target = "shopId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "address.addressId", ignore = true)
    ShopModel toShopModel(ShopCreationPayload payload);

    ShopPayload toShopPayload(ShopModel shopModel);

    List<CategoryPayload> toCategoryPayloads(List<CategoryModel> categoryModels);
}
