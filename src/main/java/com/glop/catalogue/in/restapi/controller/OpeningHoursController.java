package com.glop.catalogue.in.restapi.controller;

import com.glop.catalogue.domain.port.in.OpeningHoursPortIn;
import com.glop.catalogue.in.restapi.mapper.ShopInMapper;
import com.glop.catalogue.in.restapi.payload.shop.OpeningHoursPayload;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OpeningHoursController {
    private final OpeningHoursPortIn openingHoursPortIn;
    private final ShopInMapper shopInMapper;

    @GetMapping("/shops/{shopId}/opening-hours")
    @Operation(summary = "Liste les horaires d'ouverture d'un commerce")
    public List<OpeningHoursPayload> listOpeningHours(@PathVariable UUID shopId) {
        return shopInMapper.toOpeningHoursPayloads(openingHoursPortIn.listOpeningHours(shopId));
    }
}
