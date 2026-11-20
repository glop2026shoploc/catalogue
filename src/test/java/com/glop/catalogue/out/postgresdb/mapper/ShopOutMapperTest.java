package com.glop.catalogue.out.postgresdb.mapper;

import org.junit.jupiter.api.Test;

import com.glop.catalogue.domain.model.shop.CategoryModel;
import com.glop.catalogue.domain.model.shop.ShopAddressModel;
import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.out.postgresdb.entity.AddressEntity;
import com.glop.catalogue.out.postgresdb.entity.CategoryEntity;
import com.glop.catalogue.out.postgresdb.entity.ShopEntity;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ShopOutMapperTest {
    private final ShopOutMapper mapper = new ShopOutMapperImpl(new AddressOutMapperImpl());
    private final AddressOutMapper addressMapper = new AddressOutMapperImpl();

    @Test
    void testMapsShopCategoryToDomainModel() {
        UUID categoryId = UUID.randomUUID();
        CategoryEntity category = new CategoryEntity();
        category.setCategoryId(categoryId);
        category.setName("Presse");
        ShopEntity entity = new ShopEntity();
        entity.setCategory(category);
        entity.setShopId(UUID.randomUUID());
        entity.setName("Kiosque");
        entity.setSiretNumber("12345678900013");
        entity.setStatus(ShopStatus.VALIDATED);

        CategoryModel mapped = mapper.toShopModel(entity).getCategory();

        assertThat(mapped.getCategoryId()).isEqualTo(categoryId);
        assertThat(mapped.getName()).isEqualTo("Presse");
    }

    @Test
    void testMapsAddressEntityToModel() {
        UUID addressId = UUID.randomUUID();
        UUID cityId = UUID.randomUUID();
        AddressEntity entity = new AddressEntity();
        entity.setAddressId(addressId);
        entity.setRoad("rue des Fours");
        entity.setNumber("20");
        entity.setComplement("Bâtiment A");
        entity.setCityId(cityId);

        ShopAddressModel mapped = addressMapper.toShopAddressModel(entity);

        assertThat(mapped.getAddressId()).isEqualTo(addressId);
        assertThat(mapped.getRoad()).isEqualTo("rue des Fours");
        assertThat(mapped.getNumber()).isEqualTo("20");
        assertThat(mapped.getComplement()).isEqualTo("Bâtiment A");
        assertThat(mapped.getCityId()).isEqualTo(cityId);
    }

    @Test
    void testMapsAddressModelToEntityWithoutAddressId() {
        ShopAddressModel model = ShopAddressModel.builder()
                .addressId(UUID.randomUUID())
                .road("rue des Fours")
                .number("20")
                .complement("Bâtiment A")
                .cityId(UUID.randomUUID())
                .build();

        AddressEntity mapped = addressMapper.toAddressEntity(model);

        assertThat(mapped.getAddressId()).isNull();
        assertThat(mapped.getRoad()).isEqualTo(model.getRoad());
        assertThat(mapped.getComplement()).isEqualTo(model.getComplement());
        assertThat(mapped.getCityId()).isEqualTo(model.getCityId());
    }

    @Test
    void testMapsShopModelToEntityAndMapsItsAddress() {
        ShopModel model = ShopModel.builder()
                .name("Boulangerie Alpha")
                .siret("12345678900013")
                .status(ShopStatus.WAITING)
                .category(new CategoryModel(UUID.randomUUID(), "Boulangerie"))
                .address(ShopAddressModel.builder()
                        .addressId(UUID.randomUUID())
                        .road("rue des Fours")
                        .number("20")
                        .cityId(UUID.randomUUID())
                        .build())
                .build();

        ShopEntity mapped = mapper.toShopEntity(model);

        assertThat(mapped.getShopId()).isNull();
        assertThat(mapped.getSiretNumber()).isEqualTo(model.getSiret());
        assertThat(mapped.getCategory()).isNull();
        assertThat(mapped.getAddress().getAddressId()).isNull();
        assertThat(mapped.getAddress().getRoad()).isEqualTo("rue des Fours");
    }
}
