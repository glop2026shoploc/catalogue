package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.config.exception.ShopAlreadyExistsException;
import com.glop.catalogue.domain.model.shop.OpeningHoursModel;
import com.glop.catalogue.domain.model.shop.ShopAddressModel;
import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.domain.port.out.CategoryPortOut;
import com.glop.catalogue.domain.port.out.NotificationPortOut;
import com.glop.catalogue.domain.port.out.ShopPortOut;
import com.glop.catalogue.util.OpeningHoursNotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopServiceTest {
    private static final UUID KNOWN_CATEGORY = UUID.randomUUID();
    private static final UUID UNKNOWN_CATEGORY = UUID.randomUUID();
    private static final UUID SHOP_ID = UUID.randomUUID();

    @Mock
    private ShopPortOut shopPortOut;
    @Mock
    private CategoryPortOut categoryPortOut;
    @Mock
    private NotificationPortOut notificationPortOut;

    @InjectMocks
    private ShopService shopService;

    private ShopModel shop;

    @BeforeEach
    void setUp() {
        shop = ShopModel.builder()
                .name("MonCommercant")
                .siret("12345678900013")
                .categoryIds(List.of(KNOWN_CATEGORY, UNKNOWN_CATEGORY))
                .address(ShopAddressModel.builder().road("rue de la République").cityId(UUID.randomUUID()).build())
                .build();
    }

    @Test
    void testCreateShouldKeepOnlyKnownCategoriesAndSetWaitingStatus() {
        when(shopPortOut.existsBySiret("12345678900013")).thenReturn(false);
        when(categoryPortOut.findExistingIds(shop.getCategoryIds())).thenReturn(List.of(KNOWN_CATEGORY));
        when(shopPortOut.create(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ShopModel created = shopService.create(shop);

        assertThat(created.getCategoryIds()).containsExactly(KNOWN_CATEGORY);
        assertThat(created.getStatus()).isEqualTo(ShopStatus.WAITING);
    }

    @Test
    void testCreateShouldThrowWhenSiretAlreadyExists() {
        when(shopPortOut.existsBySiret("12345678900013")).thenReturn(true);

        assertThatThrownBy(() -> shopService.create(shop)).isInstanceOf(ShopAlreadyExistsException.class);

        verify(shopPortOut, never()).create(any());
    }

    @Test
    void testCreateWithoutCategoriesShouldNotQueryCategories() {
        shop.setCategoryIds(List.of());
        when(shopPortOut.existsBySiret(any())).thenReturn(false);
        when(shopPortOut.create(any())).thenAnswer(invocation -> invocation.getArgument(0));

        shopService.create(shop);

        verifyNoInteractions(categoryPortOut);
    }

    @Test
    void testUpdateOpeningHoursShouldReplaceAndNotifyFavoriteClients() {
        List<UUID> clientsIds = List.of(UUID.randomUUID(), UUID.randomUUID());

        List<OpeningHoursModel> newHours = List.of(
                new OpeningHoursModel(1, LocalTime.parse("09:00"), LocalTime.parse("18:00")));

        when(shopPortOut.findById(SHOP_ID)).thenReturn(shop);
        when(shopPortOut.findOpeningHours(SHOP_ID))
                .thenReturn(List.of(new OpeningHoursModel(1, LocalTime.parse("08:00"), LocalTime.parse("17:00"))));
        when(shopPortOut.findFavoriteClientIds(SHOP_ID)).thenReturn(clientsIds);

        shopService.updateOpeningHours(SHOP_ID, newHours);

        verify(shopPortOut).replaceOpeningHours(SHOP_ID, newHours);
        verify(notificationPortOut).notifyOpeningHoursUpdated(shop, clientsIds);
    }

    @Test
    void testUpdateOpeningHoursWithoutFavoritesShouldNotNotify() {
        List<OpeningHoursModel> newHours = List.of(
                new OpeningHoursModel(1, LocalTime.parse("09:00"), LocalTime.parse("18:00")));

        when(shopPortOut.findOpeningHours(SHOP_ID)).thenReturn(List.of());
        when(shopPortOut.findFavoriteClientIds(SHOP_ID)).thenReturn(List.of());

        shopService.updateOpeningHours(SHOP_ID, newHours);

        verify(shopPortOut).replaceOpeningHours(SHOP_ID, newHours);
        verifyNoInteractions(notificationPortOut);
    }

    @Test
    void testUpdateOpeningHoursWithSameHoursShouldDoNothing() {
        List<OpeningHoursModel> hours = List.of(
                new OpeningHoursModel(1, LocalTime.parse("09:00"), LocalTime.parse("18:00")));

        when(shopPortOut.findOpeningHours(SHOP_ID)).thenReturn(hours);

        shopService.updateOpeningHours(SHOP_ID, hours);

        verify(shopPortOut, never()).replaceOpeningHours(any(), any());
        verify(shopPortOut, never()).findFavoriteClientIds(any());
        verifyNoInteractions(notificationPortOut);
    }

    @Test
    void testUpdateOpeningHoursWithCloseBeforeOpenShouldThrow() {
        List<OpeningHoursModel> hours = List.of(
                new OpeningHoursModel(1, LocalTime.parse("18:00"), LocalTime.parse("08:00")));

        assertThatThrownBy(() -> shopService.updateOpeningHours(SHOP_ID, hours))
                .isInstanceOf(OpeningHoursNotValidException.class)
                .hasMessage("L'heure de fermeture doit être après l'heure d'ouverture (jour 1).");

        verify(shopPortOut, never()).replaceOpeningHours(any(), any());
        verifyNoInteractions(notificationPortOut);
    }

    @Test
    void testUpdateOpeningHoursWithCloseEqualToOpenShouldThrow() {
        List<OpeningHoursModel> hours = List.of(
                new OpeningHoursModel(1, LocalTime.parse("08:00"), LocalTime.parse("08:00")));

        assertThatThrownBy(() -> shopService.updateOpeningHours(SHOP_ID, hours))
                .isInstanceOf(OpeningHoursNotValidException.class)
                .hasMessage("L'heure de fermeture doit être après l'heure d'ouverture (jour 1).");

        verify(shopPortOut, never()).replaceOpeningHours(any(), any());
    }

    @Test
    void testUpdateOpeningHoursWithOverlappingHoursShouldThrow() {
        List<OpeningHoursModel> hours = List.of(
                new OpeningHoursModel(1, LocalTime.parse("08:00"), LocalTime.parse("13:00")),
                new OpeningHoursModel(1, LocalTime.parse("12:00"), LocalTime.parse("17:00")));

        assertThatThrownBy(() -> shopService.updateOpeningHours(SHOP_ID, hours))
                .isInstanceOf(OpeningHoursNotValidException.class)
                .hasMessage("Deux créneaux se chevauchent le jour 1.");

        verify(shopPortOut, never()).replaceOpeningHours(any(), any());
    }
}
