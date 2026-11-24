package com.glop.catalogue.out.postgresdb.repository;

import com.glop.catalogue.TestcontainersConfiguration;
import com.glop.catalogue.out.postgresdb.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import({TestcontainersConfiguration.class,DataFactory.class})
@DataJpaTest
@DisplayName("Client Favorite Shop - Repository Test")
class ClientShopFavoriteRepositoryTest {
    @Autowired
    private DataFactory dataFactory;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ClientShopFavoriteRepository clientShopFavoriteRepository;

    @Nested
    @DisplayName("Save")
    public class save{
        @Test
        @DisplayName("Create one favorite")
        void testAddFavorite() {
            //PREPARE
            UUID clientOneId = UUID.randomUUID();
            ShopEntity shopOne = dataFactory.createShopEntity();
            UUID shopOneId = shopOne.getShopId();

            ClientShopFavoriteEntity favorite = new ClientShopFavoriteEntity();
            favorite.setShop(shopOne);
            favorite.setClientId(clientOneId);
            clientShopFavoriteRepository.save(favorite);

            //CHECK
            assertThat(clientShopFavoriteRepository.existsByClientIdAndShop_ShopId(clientOneId,shopOneId)).isTrue();
        }
    }

    @Nested
    @DisplayName("Remove Method")
    public class Remove{
        @Test
        @DisplayName("Simple remove. (one user and shop)")
        void testShouldRemoveShopLineInDatabase() {
            //PREPARE
            UUID clientId = UUID.randomUUID();
            ShopEntity shop = dataFactory.createShopEntity();
            UUID shopId = shop.getShopId();

            ClientShopFavoriteEntity favorite = new ClientShopFavoriteEntity();
            favorite.setShop(shop);
            favorite.setClientId(clientId);
            favorite = clientShopFavoriteRepository.save(favorite);

            //ACT
            clientShopFavoriteRepository.deleteByClientIdAndShop_ShopId(clientId,shopId);
            //CHECK
            assertThat(clientShopFavoriteRepository.existsById(favorite.getClientShopFavoriteId())).isFalse();

        }
        @Test
        @DisplayName("Remove only remove specified shop.")
        void testShouldRemoveOnlySpecifiedShop() {
            //PREPARE
            UUID clientId = UUID.randomUUID();
            ShopEntity shopOne = dataFactory.createShopEntity();
            ShopEntity shopTwo = dataFactory.createShopEntity();
            UUID shopOneId = shopOne.getShopId();

            ClientShopFavoriteEntity favorite = new ClientShopFavoriteEntity();
            favorite.setShop(shopOne);
            favorite.setClientId(clientId);
            favorite = clientShopFavoriteRepository.save(favorite);

            ClientShopFavoriteEntity otherFavorite = new ClientShopFavoriteEntity();
            otherFavorite.setClientId(clientId);
            otherFavorite.setShop(shopTwo);
            otherFavorite = clientShopFavoriteRepository.save(otherFavorite);
            //ACT
            clientShopFavoriteRepository.deleteByClientIdAndShop_ShopId(clientId,shopOneId);
            //CHECK
            assertThat(clientShopFavoriteRepository.existsById(favorite.getClientShopFavoriteId())).isFalse();
            assertThat(clientShopFavoriteRepository.existsById(otherFavorite.getClientShopFavoriteId())).isTrue();
        }
        @Test
        @DisplayName("Remove only remove shop to the specified user.")
        void testShouldRemoveShopOnlyToTheSpecifiedUser() {
            //PREPARE
            UUID clientOneId = UUID.randomUUID();
            UUID clientTwoId = UUID.randomUUID();
            ShopEntity shop = dataFactory.createShopEntity();
            UUID shopOneId = shop.getShopId();

            ClientShopFavoriteEntity favorite = new ClientShopFavoriteEntity();
            favorite.setClientId(clientOneId);
            favorite.setShop(shop);
            favorite = clientShopFavoriteRepository.save(favorite);

            ClientShopFavoriteEntity otherFavorite = new ClientShopFavoriteEntity();
            otherFavorite.setClientId(clientTwoId);
            otherFavorite.setShop(shop);
            otherFavorite = clientShopFavoriteRepository.save(otherFavorite);
            //ACT
            clientShopFavoriteRepository.deleteByClientIdAndShop_ShopId(clientOneId,shopOneId);
            //CHECK
            assertThat(clientShopFavoriteRepository.existsById(favorite.getClientShopFavoriteId())).isFalse();
            assertThat(clientShopFavoriteRepository.existsById(otherFavorite.getClientShopFavoriteId())).isTrue();
        }

        @Test
        @DisplayName("No favorite no error.")
        void testShouldDoNothingWhenNoFavorite() {
            //ACT
            clientShopFavoriteRepository.deleteByClientIdAndShop_ShopId(UUID.randomUUID(),UUID.randomUUID());
        }
    }

    @Test
    @DisplayName("findShopIdsByClientId ne renvoie que les favoris du client demandé")
    void testFindShopIdsByClientIdShouldReturnOnlyClientFavorites() {
        UUID clientId = UUID.randomUUID();
        ShopEntity shopOne = dataFactory.createShopEntity();
        ShopEntity shopTwo = dataFactory.createShopEntity();
        ShopEntity otherClientShop = dataFactory.createShopEntity();

        for (ShopEntity shop : java.util.List.of(shopOne, shopTwo)) {
            ClientShopFavoriteEntity favorite = new ClientShopFavoriteEntity();
            favorite.setClientId(clientId);
            favorite.setShop(shop);
            clientShopFavoriteRepository.save(favorite);
        }
        ClientShopFavoriteEntity otherFavorite = new ClientShopFavoriteEntity();
        otherFavorite.setClientId(UUID.randomUUID());
        otherFavorite.setShop(otherClientShop);
        clientShopFavoriteRepository.save(otherFavorite);

        assertThat(clientShopFavoriteRepository.findShopIdsByClientId(clientId))
                .containsExactlyInAnyOrder(shopOne.getShopId(), shopTwo.getShopId());
        assertThat(clientShopFavoriteRepository.findShopIdsByClientId(UUID.randomUUID())).isEmpty();
    }

}
