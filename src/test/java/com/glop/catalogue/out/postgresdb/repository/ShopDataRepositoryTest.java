package com.glop.catalogue.out.postgresdb.repository;

import com.glop.catalogue.TestcontainersConfiguration;
import com.glop.catalogue.domain.model.shop.ShopStatus;
import com.glop.catalogue.out.postgresdb.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class ShopDataRepositoryTest {

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private OpeningHoursRepository openingHoursRepository;
    @Autowired
    private ProductTypeRepository productTypeRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void testShouldSaveAndFindOpeningHoursAndProductsOfShop() {
        AddressEntity address = new AddressEntity();
        address.setRoad("place du Général de Gaulle");
        address.setCityId(UUID.randomUUID());
        ShopEntity shop = new ShopEntity();
        shop.setName("Café du Marché");
        shop.setSiretNumber("98765432100019");
        shop.setStatus(ShopStatus.VALIDATED);
        shop.setAddress(address);
        shop = shopRepository.save(shop);
        UUID shopId = shop.getShopId();

        OpeningHoursEntity hours = new OpeningHoursEntity();
        hours.setShop(shop);
        hours.setDayOfWeek(1);
        hours.setOpenTime(LocalTime.of(8, 0));
        hours.setCloseTime(LocalTime.of(12, 30));
        openingHoursRepository.save(hours);

        ProductTypeEntity type = new ProductTypeEntity();
        type.setName("Boisson");
        type = productTypeRepository.save(type);
        ProductEntity product = new ProductEntity();
        product.setName("Café allongé");
        product.setQuantity(40);
        product.setPrice(new BigDecimal("2.50"));
        product.setEarnedPoints(0);
        product.setProductType(type);
        product.setShop(shop);
        productRepository.save(product);

        List<OpeningHoursEntity> foundHours = openingHoursRepository.findByShopShopIdOrderByDayOfWeekAscOpenTimeAsc(shopId);
        assertThat(foundHours).hasSize(1);
        assertThat(foundHours.get(0).getDayOfWeek()).isEqualTo(1);
        assertThat(foundHours.get(0).getCloseTime()).isEqualTo(LocalTime.of(12, 30));

        List<ProductEntity> foundProducts = productRepository.findByShopShopId(shopId);
        assertThat(foundProducts).hasSize(1);
        assertThat(foundProducts.get(0).getPrice()).isEqualByComparingTo("2.50");
        assertThat(foundProducts.get(0).getPointsPrice()).isNull();
    }
}
