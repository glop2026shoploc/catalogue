package com.glop.catalogue.in.restapi.config.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Builder
@Data
@Schema(name = "ErrorResponse", description = "Standard error payload returned by the API when a request fails")
public class ErrorResponse {

    @Schema(
            description = "Human-readable description of the error",
            example = "Resource not found",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private final String message;

    @Builder.Default
    @Schema(
            description = "Date and time when the error occurred, in the Europe/Paris time zone (ISO-8601 with offset)",
            example = "2026-10-11T14:07:00+02:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private final ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
}