package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.model.shop.OpeningHoursModel;
import com.glop.catalogue.domain.port.in.OpeningHoursPortIn;
import com.glop.catalogue.domain.port.out.OpeningHoursPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpeningHoursService implements OpeningHoursPortIn {
    private final OpeningHoursPortOut openingHoursPortOut;

    @Override
    public List<OpeningHoursModel> listOpeningHours(UUID shopId) {
        return openingHoursPortOut.findByShopId(shopId);
    }
}
