package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.config.exception.ShopAlreadyExistsException;
import com.glop.catalogue.domain.config.exception.ShopNotFoundException;
import com.glop.catalogue.domain.model.shop.ShopAddressModel;
import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.domain.port.out.CategoryPortOut;
import com.glop.catalogue.domain.port.out.ShopPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Mock
    private ShopPortOut shopPortOut;
    @Mock
    private CategoryPortOut categoryPortOut;
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
    void testListValidatedByCityShouldOnlyAskValidatedShops() {
        UUID cityId = UUID.randomUUID();
        when(shopPortOut.findByStatusAndCity(ShopStatus.VALIDATED, cityId)).thenReturn(List.of(shop));

        assertThat(shopService.listValidatedByCity(cityId)).containsExactly(shop);
    }

    @Nested
    @DisplayName("addOrRemoveClientFavoriteShop")
    class Favorite {
        private final UUID shopId = UUID.randomUUID();
        private final UUID clientId = UUID.randomUUID();

        @Test
        @DisplayName("Ajoute le commerce aux favoris quand isFavorite = true")
        void testShouldAddFavoriteWhenIsFavoriteIsTrue() {
            when(shopPortOut.exists(shopId)).thenReturn(true);

            shopService.addOrRemoveClientFavoriteShop(shopId, true, clientId);

            verify(shopPortOut).addToFavorite(shopId, clientId);
            verify(shopPortOut, never()).removeFavorite(any(), any());
        }

        @Test
        @DisplayName("Retire le commerce des favoris quand isFavorite = false")
        void testShouldRemoveFavoriteWhenIsFavoriteIsFalse() {
            when(shopPortOut.exists(shopId)).thenReturn(true);

            shopService.addOrRemoveClientFavoriteShop(shopId, false, clientId);

            verify(shopPortOut).removeFavorite(shopId, clientId);
            verify(shopPortOut, never()).addToFavorite(any(), any());
        }

        @ParameterizedTest(name = "isFavorite = {0}")
        @ValueSource(booleans = {true, false})
        @DisplayName("Lève ShopNotFoundException et ne touche pas aux favoris si le commerce n'existe pas")
        void testShouldThrowWhenShopDoesNotExist(boolean isFavorite) {
            when(shopPortOut.exists(shopId)).thenReturn(false);

            assertThatThrownBy(() -> shopService.addOrRemoveClientFavoriteShop(shopId, isFavorite, clientId))
                    .isInstanceOf(ShopNotFoundException.class);

            verify(shopPortOut, never()).addToFavorite(any(), any());
            verify(shopPortOut, never()).removeFavorite(any(), any());
        }
    }


}
