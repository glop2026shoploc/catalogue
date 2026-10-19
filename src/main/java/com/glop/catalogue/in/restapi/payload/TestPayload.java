package com.glop.catalogue.in.restapi.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "TestPayload", description = "Dummy test objet to test a full flow")
public class TestPayload {

    @NotBlank
    @Schema(
            description = "A dummy variable to test the mapping flow",
            example = "This is a useless message",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String testMessage1;

    @Schema(
            description = "An other dummy variable to test the mapping flow",
            example = "This is a useless message",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String testMessage2;

    @NotNull
    @Schema(
            description = "Number of hits",
            example = "1505",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer numberOfTestRequestSinceBeginning;
}