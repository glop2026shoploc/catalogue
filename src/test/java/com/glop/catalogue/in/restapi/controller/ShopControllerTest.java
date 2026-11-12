package com.glop.catalogue.in.restapi.controller;

import com.glop.catalogue.domain.config.exception.ShopAlreadyExistsException;
import com.glop.catalogue.domain.model.shop.CategoryModel;
import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.domain.port.in.CategoryPortIn;
import com.glop.catalogue.domain.port.in.ShopPortIn;
import com.glop.catalogue.in.restapi.config.error.GlobalControllerExceptionHandler;
import com.glop.catalogue.in.restapi.mapper.ShopInMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ShopControllerTest {
    private static final UUID SHOP_ID = UUID.randomUUID();
    private static final UUID CITY_ID = UUID.randomUUID();
    private static final String VALID_SHOP = """
            {"name":"MonCommerce","siret":"12345678900013","categoryIds":[],
             "address":{"road":"rue de la République","cityId":"%s"}}
            """.formatted(CITY_ID);

    @Mock
    private ShopPortIn shopPortIn;
    @Mock
    private CategoryPortIn categoryPortIn;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ShopInMapperImpl mapper = new ShopInMapperImpl();
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ShopController(shopPortIn, mapper), new CategoryController(categoryPortIn, mapper))
                .setControllerAdvice(new GlobalControllerExceptionHandler())
                .build();
    }

    @Test
    void testCreateShouldReturn201WithCreatedShop() throws Exception {
        when(shopPortIn.create(any())).thenAnswer(invocation -> {
            ShopModel shop = invocation.getArgument(0);
            shop.setShopId(SHOP_ID);
            shop.setStatus(ShopStatus.WAITING);
            return shop;
        });

        mockMvc.perform(post("/shops").contentType(MediaType.APPLICATION_JSON).content(VALID_SHOP))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shopId").value(SHOP_ID.toString()))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.address.cityId").value(CITY_ID.toString()));

        ArgumentCaptor<ShopModel> captor = ArgumentCaptor.forClass(ShopModel.class);
        verify(shopPortIn).create(captor.capture());
        assertThat(captor.getValue().getSiret()).isEqualTo("12345678900013");
        assertThat(captor.getValue().getAddress().getRoad()).isEqualTo("rue de la République");
    }

    @Test
    void testCreateWithInvalidSiretShouldReturn400() throws Exception {
        mockMvc.perform(post("/shops").contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_SHOP.replace("12345678900013", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("siret")));

        verifyNoInteractions(shopPortIn);
    }

    @Test
    void testCreateWithExistingSiretShouldReturn409() throws Exception {
        when(shopPortIn.create(any())).thenThrow(new ShopAlreadyExistsException("Un commerce existe déjà avec ce SIRET."));

        mockMvc.perform(post("/shops").contentType(MediaType.APPLICATION_JSON).content(VALID_SHOP))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Un commerce existe déjà avec ce SIRET."));
    }

    @Test
    void testCreateWithDuplicateDataShouldReturn409() throws Exception {
        when(shopPortIn.create(any())).thenThrow(new DataIntegrityViolationException("duplicate key"));

        mockMvc.perform(post("/shops").contentType(MediaType.APPLICATION_JSON).content(VALID_SHOP))
                .andExpect(status().isConflict());
    }

    @Test
    void testListCategoriesShouldReturnCategories() throws Exception {
        UUID categoryId = UUID.randomUUID();
        when(categoryPortIn.listCategories()).thenReturn(List.of(new CategoryModel(categoryId, "Café")));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryId").value(categoryId.toString()))
                .andExpect(jsonPath("$[0].name").value("Café"));
    }
}
