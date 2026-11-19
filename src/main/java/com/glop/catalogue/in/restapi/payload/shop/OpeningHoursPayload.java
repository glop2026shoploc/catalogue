package com.glop.catalogue.in.restapi.payload.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

@Data
@Schema(name = "OpeningHours", description = "Créneau d'ouverture d'un commerce")
public class OpeningHoursPayload {
    @Schema(description = "1 = lundi, 7 = dimanche", example = "1")
    private Integer dayOfWeek;
    @Schema(type = "string", example = "08:00:00")
    private LocalTime openTime;
    @Schema(type = "string", example = "12:30:00")
    private LocalTime closeTime;
}
