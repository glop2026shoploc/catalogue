package com.glop.catalogue.out.postgresdb.mapper;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ShopOutMapperTest {
    private final ShopOutMapper mapper = new ShopOutMapperImpl();

    @Test
    void testToCategoryIdsWithNullShouldReturnEmptyList() {
        assertThat(mapper.toCategoryIds(null)).isEmpty();
        assertThat(mapper.toCategoryIds(Set.of())).isEmpty();
    }
}
