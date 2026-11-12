package com.glop.catalogue.out.postgresdb.mapper;

import com.glop.catalogue.domain.model.shop.CategoryModel;
import com.glop.catalogue.domain.model.shop.ShopAddressModel;
import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.out.postgresdb.entity.AddressEntity;
import com.glop.catalogue.out.postgresdb.entity.CategoryEntity;
import com.glop.catalogue.out.postgresdb.entity.ShopEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShopOutMapper {

    @Mapping(target = "siret", source = "siretNumber")
    @Mapping(target = "categoryIds", source = "categories")
    ShopModel toShopModel(ShopEntity shopEntity);

    ShopAddressModel toShopAddressModel(AddressEntity addressEntity);

    /** Les catégories sont rattachées par l'adapter. */
    @Mapping(target = "shopId", ignore = true)
    @Mapping(target = "siretNumber", source = "siret")
    @Mapping(target = "categories", ignore = true)
    ShopEntity toShopEntity(ShopModel shopModel);

    @Mapping(target = "addressId", ignore = true)
    AddressEntity toAddressEntity(ShopAddressModel addressModel);

    CategoryModel toCategoryModel(CategoryEntity categoryEntity);

    List<CategoryModel> toCategoryModels(List<CategoryEntity> categoryEntities);

    default List<UUID> toCategoryIds(Set<CategoryEntity> categories) {
        return categories == null ? List.of() : categories.stream().map(CategoryEntity::getCategoryId).toList();
    }
}
