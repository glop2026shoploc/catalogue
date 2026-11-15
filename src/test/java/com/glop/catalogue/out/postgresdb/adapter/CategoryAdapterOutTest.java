package com.glop.catalogue.out.postgresdb.adapter;

import com.glop.catalogue.domain.model.shop.CategoryModel;
import com.glop.catalogue.out.postgresdb.entity.CategoryEntity;
import com.glop.catalogue.out.postgresdb.mapper.ShopOutMapperImpl;
import com.glop.catalogue.out.postgresdb.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryAdapterOutTest {
    private static final UUID CATEGORY_ID = UUID.randomUUID();

    @Mock
    private CategoryRepository categoryRepository;
    private CategoryAdapterOut adapter;
    private CategoryEntity category;

    @BeforeEach
    void setUp() {
        adapter = new CategoryAdapterOut(categoryRepository, new ShopOutMapperImpl());
        category = new CategoryEntity();
        category.setCategoryId(CATEGORY_ID);
        category.setName("Café");
    }

    @Test
    void testFindAllShouldReturnCategoriesSortedByName() {
        when(categoryRepository.findAll(Sort.by("name"))).thenReturn(List.of(category));

        List<CategoryModel> categories = adapter.findAll();

        assertThat(categories).containsExactly(new CategoryModel(CATEGORY_ID, "Café"));
    }

    @Test
    void testFindExistingIdsShouldReturnOnlyIdsFoundInDatabase() {
        UUID unknown = UUID.randomUUID();
        when(categoryRepository.findAllById(List.of(CATEGORY_ID, unknown))).thenReturn(List.of(category));

        assertThat(adapter.findExistingIds(List.of(CATEGORY_ID, unknown))).containsExactly(CATEGORY_ID);
    }
}
