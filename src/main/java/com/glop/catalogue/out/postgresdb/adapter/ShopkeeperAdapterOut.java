package com.glop.catalogue.out.postgresdb.adapter;

import com.glop.catalogue.domain.model.ShopkeeperModel;
import com.glop.catalogue.domain.port.out.ShopkeeperPortOut;
import com.glop.catalogue.out.postgresdb.entity.ShopkeeperEntity;
import com.glop.catalogue.out.postgresdb.repository.ShopkeeperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ShopkeeperAdapterOut implements ShopkeeperPortOut {
    private final ShopkeeperRepository shopkeeperRepository;

    @Override
    public List<ShopkeeperModel> findAll() {
        return shopkeeperRepository.findAllByOrderByNameAsc().stream()
                .map(this::toModel)
                .toList();
    }

    @Override
    public List<ShopkeeperModel> findByCategory(String category) {
        return shopkeeperRepository.findByCategoryIgnoreCaseOrderByNameAsc(category).stream()
                .map(this::toModel)
                .toList();
    }

    private ShopkeeperModel toModel(ShopkeeperEntity entity) {
        return new ShopkeeperModel(
                entity.getId().toString(),
                entity.getName(),
                entity.getCategory(),
                entity.getAddress());
    }
}
