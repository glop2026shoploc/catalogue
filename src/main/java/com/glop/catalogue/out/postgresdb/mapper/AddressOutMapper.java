package com.glop.catalogue.out.postgresdb.mapper;

import com.glop.catalogue.domain.model.shop.ShopAddressModel;
import com.glop.catalogue.out.postgresdb.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AddressOutMapper {
    ShopAddressModel toShopAddressModel(AddressEntity addressEntity);

    @Mapping(target = "addressId", ignore = true)
    AddressEntity toAddressEntity(ShopAddressModel addressModel);
}
