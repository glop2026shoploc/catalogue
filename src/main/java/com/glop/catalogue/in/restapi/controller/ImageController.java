package com.glop.catalogue.in.restapi.controller;

import com.glop.catalogue.domain.port.in.ImagePortIn;
import com.glop.catalogue.in.restapi.config.error.ErrorResponse;
import com.glop.catalogue.in.restapi.payload.ImagePayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final ImagePortIn imagePortIn;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload an image")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Image successfully uploaded",
                    content = @Content(schema = @Schema(implementation = ImagePayload.class))),
            @ApiResponse(responseCode = "400", description = "Invalid or unsupported file",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Something went wrong...",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ImagePayload upload(@RequestPart("file") MultipartFile file) throws IOException {
        try (InputStream content = file.getInputStream()) {
            String id = imagePortIn.upload(content, file.getSize(), file.getContentType());
            return new ImagePayload(id, imagePortIn.getDownloadUrl(id).toString());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an image (redirects to a temporary URL)")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Redirect to the image"),
            @ApiResponse(responseCode = "500", description = "Something went wrong...",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<Void> get(@PathVariable String id) {
        URI location = URI.create(imagePortIn.getDownloadUrl(id).toString());
        return ResponseEntity.status(HttpStatus.FOUND).location(location).build();
    }
}