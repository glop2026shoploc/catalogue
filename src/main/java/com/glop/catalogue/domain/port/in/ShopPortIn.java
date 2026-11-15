package com.glop.catalogue.domain.port.in;

import com.glop.catalogue.domain.model.shop.ShopModel;
import com.glop.catalogue.domain.model.shop.ShopStatus;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface ShopPortIn {
    /** Crée un commerce en attente de validation */
    ShopModel create(ShopModel shop);

    List<ShopModel> listWaiting();

    ShopModel updateStatus(UUID shopId, ShopStatus status);

    void uploadDocument(UUID shopId, InputStream content, long size, String contentType);
}
