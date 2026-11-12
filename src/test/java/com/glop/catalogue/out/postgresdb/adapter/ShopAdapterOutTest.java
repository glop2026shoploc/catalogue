package com.glop.catalogue.out.postgresdb.adapter;

import com.glop.catalogue.domain.model.shop.ShopAddressModel;
import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.out.postgresdb.entity.CategoryEntity;
import com.glop.catalogue.out.postgresdb.entity.ShopEntity;
import com.glop.catalogue.out.postgresdb.mapper.ShopOutMapperImpl;
import com.glop.catalogue.out.postgresdb.repository.CategoryRepository;
import com.glop.catalogue.out.postgresdb.repository.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopAdapterOutTest {
    private static final UUID CATEGORY_ID = UUID.randomUUID();
    private static final UUID CITY_ID = UUID.randomUUID();

    @Mock
    private ShopRepository shopRepository;
    @Mock
    private CategoryRepository categoryRepository;
    private ShopAdapterOut adapter;

    @BeforeEach
    void setUp() {
        adapter = new ShopAdapterOut(shopRepository, categoryRepository, new ShopOutMapperImpl());
    }

    private static ShopModel shop(List<UUID> categoryIds) {
        return ShopModel.builder()
                .name("MonCommerce")
                .siret("12345678900013")
                .status(ShopStatus.WAITING)
                .categoryIds(categoryIds)
                .address(ShopAddressModel.builder().road("rue de la République").number("12").cityId(CITY_ID).build())
                .build();
    }

    @Test
    void testExistsBySiretShouldAskRepository() {
        when(shopRepository.existsBySiretNumber("12345678900013")).thenReturn(true);

        assertThat(adapter.existsBySiret("12345678900013")).isTrue();
    }

    @Test
    void testCreateShouldSaveShopWithAddressAndCategories() {
        CategoryEntity category = new CategoryEntity();
        category.setCategoryId(CATEGORY_ID);
        when(categoryRepository.findAllById(List.of(CATEGORY_ID))).thenReturn(List.of(category));
        when(shopRepository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ShopModel created = adapter.create(shop(List.of(CATEGORY_ID)));

        ArgumentCaptor<ShopEntity> captor = ArgumentCaptor.forClass(ShopEntity.class);
        verify(shopRepository).saveAndFlush(captor.capture());
        ShopEntity saved = captor.getValue();
        assertThat(saved.getSiretNumber()).isEqualTo("12345678900013");
        assertThat(saved.getAddress().getCityId()).isEqualTo(CITY_ID);
        assertThat(saved.getCategories()).containsExactly(category);
        assertThat(created.getCategoryIds()).containsExactly(CATEGORY_ID);
        assertThat(created.getAddress().getRoad()).isEqualTo("rue de la République");
    }

    @Test
    void testCreateWithoutCategoriesShouldNotQueryCategories() {
        when(shopRepository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ShopModel created = adapter.create(shop(List.of()));

        verifyNoInteractions(categoryRepository);
        assertThat(created.getCategoryIds()).isEmpty();
    }
}
