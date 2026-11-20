package com.glop.catalogue;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class ShopFilterIntegrationTest {
    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    private final UUID bakeryId = UUID.randomUUID();
    private final UUID pressId = UUID.randomUUID();
    private final UUID bakeryAddressId = UUID.randomUUID();
    private final UUID pressAddressId = UUID.randomUUID();
    private final UUID waitingAddressId = UUID.randomUUID();
    private final UUID otherAddressId = UUID.randomUUID();
    private final UUID secondBakeryAddressId = UUID.randomUUID();
    private final UUID bakeryShopId = UUID.randomUUID();
    private final UUID pressShopId = UUID.randomUUID();
    private final UUID waitingShopId = UUID.randomUUID();
    private final UUID otherShopId = UUID.randomUUID();
    private final UUID secondBakeryShopId = UUID.randomUUID();
    private String bakerySiret;
    private String pressSiret;
    private String waitingSiret;
    private String otherSiret;
    private final UUID cityId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/catalogue";
        bakerySiret = newSiret();
        pressSiret = newSiret();
        waitingSiret = newSiret();
        otherSiret = newSiret();

        jdbc.update("INSERT INTO category(category_id, name) VALUES (?, ?)", bakeryId, "TestBoulangerie" + bakeryId);
        jdbc.update("INSERT INTO category(category_id, name) VALUES (?, ?)", pressId, "TestPresse" + pressId);
        insertAddress(bakeryAddressId, "rue Nationale", "3");
        insertAddress(pressAddressId, "place du Théâtre", "12");
        insertAddress(waitingAddressId, "rue en attente", "1");
        insertAddress(otherAddressId, "rue rejetée", "2");
        insertAddress(secondBakeryAddressId, "rue des Fours", "20", "Bâtiment A");
        insertShop(bakeryShopId, "Chez Paul", bakerySiret, "VALIDATED", bakeryAddressId, bakeryId);
        insertShop(pressShopId, "Le Kiosque", pressSiret, "VALIDATED", pressAddressId, pressId);
        insertShop(waitingShopId, "Commerce en attente", waitingSiret, "WAITING", waitingAddressId, bakeryId);
        insertShop(otherShopId, "Commerce rejeté", otherSiret, "REJECTED", otherAddressId, bakeryId);
        insertShop(secondBakeryShopId, "Boulangerie Alpha", newSiret(), "VALIDATED", secondBakeryAddressId, bakeryId);
    }

    @AfterEach
    void tearDown() {
        jdbc.update("DELETE FROM shop WHERE shop_id IN (?, ?, ?, ?, ?)", bakeryShopId, pressShopId, waitingShopId, otherShopId, secondBakeryShopId);
        jdbc.update("DELETE FROM address WHERE address_id IN (?, ?, ?, ?, ?)", bakeryAddressId, pressAddressId, waitingAddressId, otherAddressId, secondBakeryAddressId);
        jdbc.update("DELETE FROM category WHERE category_id IN (?, ?)", bakeryId, pressId);
    }

    @Test
    void shouldReturnOnlyValidatedShops() {
        given().when().get("/shops")
                .then().statusCode(200)
                .body("name", hasItems("Chez Paul", "Le Kiosque", "Boulangerie Alpha"))
                .body("name", not(hasItems("Commerce en attente", "Commerce rejeté")));
    }

    @Test
    void shouldFilterShopsByCategoryId() {
        given().queryParam("categoryId", bakeryId)
                .when().get("/shops")
                .then().statusCode(200)
                .body("$", hasSize(2))
                .body("name", contains("Boulangerie Alpha", "Chez Paul"))
                .body("[0].category.categoryId", equalTo(bakeryId.toString()))
                .body("[0].category.name", startsWith("TestBoulangerie"));
    }

    @Test
    void shouldSortFilteredShopsByName() {
        given().queryParam("categoryId", bakeryId)
                .when().get("/shops")
                .then().statusCode(200)
                .body("name", contains("Boulangerie Alpha", "Chez Paul"));
    }

    @Test
    void shouldReturnFullAddressIncludingComplementAndCityId() {
        given().queryParam("categoryId", bakeryId)
                .when().get("/shops")
                .then().statusCode(200)
                .body("find { it.name == 'Boulangerie Alpha' }.address.road", equalTo("rue des Fours"))
                .body("find { it.name == 'Boulangerie Alpha' }.address.number", equalTo("20"))
                .body("find { it.name == 'Boulangerie Alpha' }.address.complement", equalTo("Bâtiment A"))
                .body("find { it.name == 'Boulangerie Alpha' }.address.cityId", equalTo(cityId.toString()));
    }

    @Test
    void shouldReturnEmptyListForCategoryWithoutValidatedShops() {
        given().queryParam("categoryId", UUID.randomUUID())
                .when().get("/shops")
                .then().statusCode(200)
                .body("$", empty());
    }

    private void insertAddress(UUID id, String road, String number) {
        insertAddress(id, road, number, null);
    }

    private void insertAddress(UUID id, String road, String number, String complement) {
        jdbc.update("INSERT INTO address(address_id, road, number, complement, city_id) VALUES (?, ?, ?, ?, ?)",
                id, road, number, complement, cityId);
    }

    private void insertShop(UUID id, String name, String siret, String status, UUID addressId, UUID categoryId) {
        jdbc.update("INSERT INTO shop(shop_id, name, siret_number, status, address_id, category_id) VALUES (?, ?, ?, ?, ?, ?)",
                id, name, siret, status, addressId, categoryId);
    }

    private String newSiret() {
        return String.format("%014d", ThreadLocalRandom.current().nextLong(1_000_000_000_000L, 99_999_999_999_999L));
    }
}
