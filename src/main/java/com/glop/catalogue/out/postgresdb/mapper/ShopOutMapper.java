package com.glop.catalogue.out.postgresdb.mapper;

import com.glop.catalogue.domain.model.shop.CategoryModel;
import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.out.postgresdb.entity.CategoryEntity;
import com.glop.catalogue.out.postgresdb.entity.ShopEntity;
import org.mapstruct.Mapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = AddressOutMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ShopOutMapper {

    @Mapping(target = "siret", source = "siretNumber")
    ShopModel toShopModel(ShopEntity shopEntity);

    List<ShopModel> toShopModels(List<ShopEntity> shopEntities);

    /** Les catégories sont rattachées par l'adapter. */
    @Mapping(target = "shopId", ignore = true)
    @Mapping(target = "siretNumber", source = "siret")
    @Mapping(target = "category", ignore = true)
    ShopEntity toShopEntity(ShopModel shopModel);

    CategoryModel toCategoryModel(CategoryEntity categoryEntity);

    List<CategoryModel> toCategoryModels(List<CategoryEntity> categoryEntities);

}
