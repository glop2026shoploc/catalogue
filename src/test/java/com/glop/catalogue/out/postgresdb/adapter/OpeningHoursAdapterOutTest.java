package com.glop.catalogue.out.postgresdb.adapter;

import com.glop.catalogue.domain.model.shop.OpeningHoursModel;
import com.glop.catalogue.out.postgresdb.entity.OpeningHoursEntity;
import com.glop.catalogue.out.postgresdb.mapper.ShopOutMapperImpl;
import com.glop.catalogue.out.postgresdb.repository.OpeningHoursRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpeningHoursAdapterOutTest {
    @Mock
    private OpeningHoursRepository openingHoursRepository;

    @Test
    void testFindByShopIdShouldReturnOpeningHoursOfShop() {
        UUID shopId = UUID.randomUUID();
        OpeningHoursEntity hours = new OpeningHoursEntity();
        hours.setDayOfWeek(1);
        hours.setOpenTime(LocalTime.of(8, 0));
        hours.setCloseTime(LocalTime.of(12, 30));
        when(openingHoursRepository.findByShopShopIdOrderByDayOfWeekAscOpenTimeAsc(shopId)).thenReturn(List.of(hours));
        OpeningHoursAdapterOut adapter = new OpeningHoursAdapterOut(openingHoursRepository, new ShopOutMapperImpl());

        List<OpeningHoursModel> result = adapter.findByShopId(shopId);

        assertThat(result).containsExactly(new OpeningHoursModel(1, LocalTime.of(8, 0), LocalTime.of(12, 30)));
    }
}
