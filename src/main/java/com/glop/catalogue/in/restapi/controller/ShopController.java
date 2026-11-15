package com.glop.catalogue.in.restapi.controller;

import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.domain.port.in.ShopPortIn;
import com.glop.catalogue.in.restapi.mapper.ShopInMapper;
import com.glop.catalogue.in.restapi.payload.shop.ShopCreationPayload;
import com.glop.catalogue.in.restapi.payload.shop.ShopPayload;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ShopController {
    private final ShopPortIn shopPortIn;
    private final ShopInMapper shopInMapper;

    /** Called par le service users à l'inscription d'un commerçant. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crée un commerce en attente de validation")
    public ShopPayload create(@Valid @RequestBody ShopCreationPayload payload) {
        return shopInMapper.toShopPayload(shopPortIn.create(shopInMapper.toShopModel(payload)));
    }

    @GetMapping("/admin/waiting")
    @Operation(summary = "Liste les commerces en attente de validation")
    public List<ShopPayload> listWaiting() {
        return shopInMapper.toShopPayloads(shopPortIn.listWaiting());
    }

    @PatchMapping("/admin/{shopId}/status")
    @Operation(summary = "Accepte ou refuse un commerce en attente")
    public ShopPayload updateStatus(@PathVariable UUID shopId, @RequestParam ShopStatus status) {
        return shopInMapper.toShopPayload(shopPortIn.updateStatus(shopId, status));
    }

    @PostMapping(value = "/{shopId}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ajoute une pièce justificative au commerce")
    public void uploadDocument(@PathVariable UUID shopId, @RequestPart("file") MultipartFile file) throws IOException {
        try (InputStream content = file.getInputStream()) {
            shopPortIn.uploadDocument(shopId, content, file.getSize(), file.getContentType());
        }
    }
}
