package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.config.exception.ShopAlreadyExistsException;
import com.glop.catalogue.domain.model.shop.ShopAddressModel;
import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.domain.port.out.CategoryPortOut;
import com.glop.catalogue.domain.port.out.FileStorage;
import com.glop.catalogue.domain.port.out.ShopPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopServiceTest {
    private static final UUID KNOWN_CATEGORY = UUID.randomUUID();
    private static final UUID UNKNOWN_CATEGORY = UUID.randomUUID();

    @Mock
    private ShopPortOut shopPortOut;
    @Mock
    private CategoryPortOut categoryPortOut;
    @Mock
    private FileStorage fileStorage;
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
    void testListWaitingShouldReturnWaitingShops() {
        when(shopPortOut.findByStatus(ShopStatus.WAITING)).thenReturn(List.of(shop));

        List<ShopModel> result = shopService.listWaiting();

        assertThat(result).containsExactly(shop);
    }

    @Test
    void testUpdateStatusShouldThrowWhenNewStatusIsWaiting() {
        UUID shopId = UUID.randomUUID();

        assertThatThrownBy(() -> shopService.updateStatus(shopId, ShopStatus.WAITING))
                .isInstanceOf(ResponseStatusException.class);

        verify(shopPortOut, never()).updateStatus(any(), any());
    }

    @Test
    void testUpdateStatusShouldThrowWhenShopNotFound() {
        UUID shopId = UUID.randomUUID();
        when(shopPortOut.findById(shopId)).thenReturn(null);

        assertThatThrownBy(() -> shopService.updateStatus(shopId, ShopStatus.VALIDATED))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Commerce introuvable.");

        verify(shopPortOut, never()).updateStatus(any(), any());
    }

    @Test
    void testUpdateStatusShouldThrowWhenAlreadyValidatedOrRejected() {
        UUID shopId = UUID.randomUUID();
        shop.setStatus(ShopStatus.REJECTED);
        when(shopPortOut.findById(shopId)).thenReturn(shop);

        assertThatThrownBy(() -> shopService.updateStatus(shopId, ShopStatus.VALIDATED))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("déjà été validé ou refusé");

        verify(shopPortOut, never()).updateStatus(any(), any());
    }

    @Test
    void testUpdateStatusShouldSaveNewStatus() {
        UUID shopId = UUID.randomUUID();
        shop.setStatus(ShopStatus.WAITING);
        when(shopPortOut.findById(shopId)).thenReturn(shop);

        shopService.updateStatus(shopId, ShopStatus.VALIDATED);

        verify(shopPortOut).updateStatus(shopId, ShopStatus.VALIDATED);
    }

    @Test
    void testUploadDocumentShouldStoreFileInShopFolder() {
        UUID shopId = UUID.randomUUID();
        InputStream content = new ByteArrayInputStream(new byte[10]);
        when(shopPortOut.findById(shopId)).thenReturn(shop);

        shopService.uploadDocument(shopId, content, 10, "application/pdf");

        verify(fileStorage).upload(startsWith("shops/" + shopId + "/documents/"), eq(content), eq(10L), eq("application/pdf"));
    }

    @Test
    void testUploadDocumentShouldThrowWhenTypeIsNotAllowed() {
        UUID shopId = UUID.randomUUID();
        InputStream content = new ByteArrayInputStream(new byte[10]);

        assertThatThrownBy(() -> shopService.uploadDocument(shopId, content, 10, "text/plain"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("PDF, un JPEG ou un PNG");

        verifyNoInteractions(fileStorage);
    }

    @Test
    void testUploadDocumentShouldThrowWhenFileIsTooBig() {
        UUID shopId = UUID.randomUUID();
        InputStream content = new ByteArrayInputStream(new byte[10]);

        assertThatThrownBy(() -> shopService.uploadDocument(shopId, content, 5 * 1024 * 1024, "application/pdf"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("4 Mo");

        verifyNoInteractions(fileStorage);
    }

    @Test
    void testUploadDocumentShouldThrowWhenShopNotFound() {
        UUID shopId = UUID.randomUUID();
        InputStream content = new ByteArrayInputStream(new byte[10]);
        when(shopPortOut.findById(shopId)).thenReturn(null);

        assertThatThrownBy(() -> shopService.uploadDocument(shopId, content, 10, "application/pdf"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Commerce introuvable.");

        verify(fileStorage, never()).upload(any(), any(), anyLong(), any());
    }

    @Test
    void testGetDocumentUrlsShouldReturnOneLinkPerFile() throws Exception {
        UUID shopId = UUID.randomUUID();
        String key = "shops/" + shopId + "/documents/kbis.pdf";
        when(shopPortOut.findById(shopId)).thenReturn(shop);
        when(fileStorage.list("shops/" + shopId + "/documents/")).thenReturn(List.of(key));
        when(fileStorage.presignedDownloadUrl(key, Duration.ofMinutes(10))).thenReturn(new URL("http://garage/kbis.pdf"));

        List<String> result = shopService.getDocumentUrls(shopId);

        assertThat(result).containsExactly("http://garage/kbis.pdf");
    }

    @Test
    void testGetDocumentUrlsShouldThrowWhenShopNotFound() {
        UUID shopId = UUID.randomUUID();
        when(shopPortOut.findById(shopId)).thenReturn(null);

        assertThatThrownBy(() -> shopService.getDocumentUrls(shopId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Commerce introuvable.");

        verifyNoInteractions(fileStorage);
    }

    @Test
    void testListValidatedByCityShouldOnlyAskValidatedShops() {
        UUID cityId = UUID.randomUUID();
        when(shopPortOut.findByStatusAndCity(ShopStatus.VALIDATED, cityId)).thenReturn(List.of(shop));

        assertThat(shopService.listValidatedByCity(cityId)).containsExactly(shop);
    }
}
