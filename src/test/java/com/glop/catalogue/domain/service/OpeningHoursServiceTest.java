package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.model.shop.OpeningHoursModel;
import com.glop.catalogue.domain.port.out.OpeningHoursPortOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpeningHoursServiceTest {
    @Mock
    private OpeningHoursPortOut openingHoursPortOut;
    @InjectMocks
    private OpeningHoursService openingHoursService;

    @Test
    void testListOpeningHoursShouldReturnOpeningHoursFromPort() {
        UUID shopId = UUID.randomUUID();
        List<OpeningHoursModel> openingHours = List.of(new OpeningHoursModel(1, LocalTime.of(8, 0), LocalTime.of(12, 30)));
        when(openingHoursPortOut.findByShopId(shopId)).thenReturn(openingHours);

        assertThat(openingHoursService.listOpeningHours(shopId)).isEqualTo(openingHours);
    }
}
