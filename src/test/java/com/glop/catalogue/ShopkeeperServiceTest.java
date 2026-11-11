package com.glop.catalogue;

import com.glop.catalogue.domain.model.ShopkeeperModel;
import com.glop.catalogue.domain.port.out.ShopkeeperPortOut;
import com.glop.catalogue.domain.service.ShopkeeperService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopkeeperServiceTest {
    @Mock ShopkeeperPortOut portOut;
    @InjectMocks ShopkeeperService service;

    private final ShopkeeperModel shop = new ShopkeeperModel("1", "Chez Paul", "Boulangerie", "3 rue Nationale");

    @Test
    void returnsAllWhenCategoryIsMissingOrBlank() {
        when(portOut.findAll()).thenReturn(List.of(shop));

        assertThat(service.findShopkeepers(null)).containsExactly(shop);
        assertThat(service.findShopkeepers("  ")).containsExactly(shop);

        verify(portOut, times(2)).findAll();
        verify(portOut, never()).findByCategory(any());
    }

    @Test
    void filtersByCategory() {
        when(portOut.findByCategory("Boulangerie")).thenReturn(List.of(shop));

        assertThat(service.findShopkeepers(" Boulangerie ")).containsExactly(shop);

        verify(portOut).findByCategory("Boulangerie");
        verify(portOut, never()).findAll();
    }

    @Test
    void returnsEmptyWhenCategoryHasNoShop() {
        when(portOut.findByCategory("Inconnue")).thenReturn(List.of());

        assertThat(service.findShopkeepers("Inconnue")).isEmpty();
    }
}
