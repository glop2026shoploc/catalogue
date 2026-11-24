package com.glop.catalogue.out.postgresdb.adapter;

import com.glop.catalogue.domain.model.shop.OpeningHoursModel;
import com.glop.catalogue.domain.model.shop.ShopAddressModel;
import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.out.postgresdb.entity.CategoryEntity;
import com.glop.catalogue.out.postgresdb.entity.ClientShopFavoriteEntity;
import com.glop.catalogue.out.postgresdb.entity.OpeningHoursEntity;
import com.glop.catalogue.out.postgresdb.entity.ShopEntity;
import com.glop.catalogue.out.postgresdb.mapper.ShopOutMapperImpl;
import com.glop.catalogue.out.postgresdb.repository.CategoryRepository;
import com.glop.catalogue.out.postgresdb.repository.ClientShopFavoriteRepository;
import com.glop.catalogue.out.postgresdb.repository.OpeningHoursRepository;
import com.glop.catalogue.out.postgresdb.repository.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopAdapterOutTest {
    private static final UUID CATEGORY_ID = UUID.randomUUID();
    private static final UUID CITY_ID = UUID.randomUUID();
    private static final UUID SHOP_ID = UUID.randomUUID();

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private ClientShopFavoriteRepository clientShopFavoriteRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private OpeningHoursRepository openingHoursRepository;
    private ShopAdapterOut adapter;

    @BeforeEach
    void setUp() {
        adapter = new ShopAdapterOut(shopRepository, clientShopFavoriteRepository,categoryRepository, new ShopOutMapperImpl(), openingHoursRepository);
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

    private static OpeningHoursEntity openingHoursEntity(int day, String open, String close) {
        OpeningHoursEntity openingHoursEntity = new OpeningHoursEntity();
        openingHoursEntity.setDayOfWeek(day);
        openingHoursEntity.setOpenTime(LocalTime.parse(open));
        openingHoursEntity.setCloseTime(LocalTime.parse(close));

        return openingHoursEntity;
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

    @Test
    void testFindOpeningHoursShouldMapEntitiesToModel() {
        when(openingHoursRepository.findByShopShopId(SHOP_ID)).thenReturn(List.of(openingHoursEntity(1, "09:00", "18:00")));

        List<OpeningHoursModel> hours = adapter.findOpeningHours(SHOP_ID);

        assertThat(hours).containsExactly(new OpeningHoursModel(1, LocalTime.of(9, 0), LocalTime.of(18, 0)));
    }

    @Test
    void testFindFavoriteClientIdsShouldReturnClientsIds() {
        UUID client1 = UUID.randomUUID();
        UUID client2 = UUID.randomUUID();

        when(clientShopFavoriteRepository.findAllByShop_ShopId(SHOP_ID)).thenReturn(
                List.of(new ClientShopFavoriteEntity(null, null, client1),
                        new ClientShopFavoriteEntity(null, null, client2))
        );

        assertThat(adapter.findFavoriteClientIds(SHOP_ID)).containsExactly(client1, client2);
    }

    /** Test généré par IA **/
    @Test
    void testReplaceOpeningHoursShouldDeleteOldThenSaveNewLinkedToShop() {
        ShopEntity shopEntity = new ShopEntity();
        shopEntity.setShopId(SHOP_ID);
        List<OpeningHoursEntity> oldHours = List.of(openingHoursEntity(1, "08:00", "12:00"));
        when(openingHoursRepository.findByShopShopId(SHOP_ID)).thenReturn(oldHours);
        when(shopRepository.findById(SHOP_ID)).thenReturn(Optional.of(shopEntity));

        adapter.replaceOpeningHours(SHOP_ID, List.of(new OpeningHoursModel(2, LocalTime.of(9, 0), LocalTime.of(18, 0))));

        InOrder inOrder = inOrder(openingHoursRepository);
        inOrder.verify(openingHoursRepository).deleteAll(oldHours);
        inOrder.verify(openingHoursRepository).flush();
        ArgumentCaptor<OpeningHoursEntity> captor = ArgumentCaptor.forClass(OpeningHoursEntity.class);
        inOrder.verify(openingHoursRepository).save(captor.capture());

        OpeningHoursEntity saved = captor.getValue();
        assertThat(saved.getShop()).isSameAs(shopEntity);
        assertThat(saved.getDayOfWeek()).isEqualTo(2);
        assertThat(saved.getOpenTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(saved.getCloseTime()).isEqualTo(LocalTime.of(18, 0));
        assertThat(saved.getOpeningHoursId()).isNull();
    }
}
