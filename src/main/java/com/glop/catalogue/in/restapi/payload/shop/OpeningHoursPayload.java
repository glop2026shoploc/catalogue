package com.glop.catalogue.in.restapi.payload.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
@Schema(name = "OpeningHours", description = "Heures d'ouverture du commerce")
public class OpeningHoursPayload {
    @NotNull
    @Min(1)
    @Max(7)
    @Schema(description = "1 = lundi, 7 = dimanche", example = "1")
    private Integer dayOfWeek;

    @NotNull
    @Schema(type = "string", example = "09:00")
    private LocalTime openTime;

    @NotNull
    @Schema(type = "string", example = "18:30")
    private LocalTime closeTime;

}
