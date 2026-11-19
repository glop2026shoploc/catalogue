package com.glop.catalogue.domain.port.in;

import com.glop.catalogue.domain.model.shop.OpeningHoursModel;

import java.util.List;
import java.util.UUID;

public interface OpeningHoursPortIn {
    List<OpeningHoursModel> listOpeningHours(UUID shopId);
}
