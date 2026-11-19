package com.glop.catalogue.out.postgresdb.adapter;

import com.glop.catalogue.domain.model.shop.OpeningHoursModel;
import com.glop.catalogue.domain.port.out.OpeningHoursPortOut;
import com.glop.catalogue.out.postgresdb.mapper.ShopOutMapper;
import com.glop.catalogue.out.postgresdb.repository.OpeningHoursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OpeningHoursAdapterOut implements OpeningHoursPortOut {
    private final OpeningHoursRepository openingHoursRepository;
    private final ShopOutMapper shopOutMapper;

    @Override
    public List<OpeningHoursModel> findByShopId(UUID shopId) {
        return shopOutMapper.toOpeningHoursModels(openingHoursRepository.findByShopShopIdOrderByDayOfWeekAscOpenTimeAsc(shopId));
    }
}
