package com.glop.catalogue.out.postgresdb.adapter;

import com.glop.catalogue.domain.model.ShopkeeperModel;
import com.glop.catalogue.domain.port.out.ShopkeeperPortOut;
import com.glop.catalogue.out.postgresdb.entity.ShopkeeperEntity;
import com.glop.catalogue.out.postgresdb.entity.AddressEntity;
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
        return shopkeeperRepository.findByCategory_NameIgnoreCaseOrderByNameAsc(category).stream()
                .map(this::toModel)
                .toList();
    }

    private ShopkeeperModel toModel(ShopkeeperEntity entity) {
        return new ShopkeeperModel(
                entity.getShopId().toString(),
                entity.getName(),
                entity.getCategory().getName(),
                formatAddress(entity.getAddress()));
    }

    private String formatAddress(AddressEntity address) {
        String number = address.getNumber() == null ? "" : address.getNumber() + " ";
        String complement = address.getComplement() == null ? "" : " " + address.getComplement();
        return number + address.getRoad() + complement;
    }
}
