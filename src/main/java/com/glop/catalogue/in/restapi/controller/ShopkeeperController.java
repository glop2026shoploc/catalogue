package com.glop.catalogue.in.restapi.controller;

import com.glop.catalogue.domain.port.in.ShopkeeperPortIn;
import com.glop.catalogue.in.restapi.payload.ShopkeeperPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/shopkeepers")
@RequiredArgsConstructor
public class ShopkeeperController {
    private final ShopkeeperPortIn shopkeeperPortIn;

    @GetMapping
    public List<ShopkeeperPayload> findShopkeepers(
            @RequestParam(required = false) String category) {
        return shopkeeperPortIn.findShopkeepers(category).stream()
                .map(shopkeeper -> new ShopkeeperPayload(
                        shopkeeper.getId(),
                        shopkeeper.getName(),
                        shopkeeper.getCategory(),
                        shopkeeper.getAddress()))
                .toList();
    }
}
