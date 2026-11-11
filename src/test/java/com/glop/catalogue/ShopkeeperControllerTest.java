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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class ShopkeeperControllerTest {
    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    private final UUID catBakery = UUID.randomUUID();
    private final UUID catPress = UUID.randomUUID();
    private final UUID shopBakery = UUID.randomUUID();
    private final UUID shopPress = UUID.randomUUID();
    private final UUID addrBakery = UUID.randomUUID();
    private final UUID addrPress = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/catalogue";

        jdbc.update("INSERT INTO category(category_id, name) VALUES (?, ?)", catBakery, "TestBoulangerie");
        jdbc.update("INSERT INTO category(category_id, name) VALUES (?, ?)", catPress, "TestPresse");
        jdbc.update("INSERT INTO address(address_id, road, number, city_id) VALUES (?, ?, ?, ?)",
                addrBakery, "rue Nationale", "3", UUID.randomUUID());
        jdbc.update("INSERT INTO address(address_id, road, number, city_id) VALUES (?, ?, ?, ?)",
                addrPress, "place du Théâtre", "12", UUID.randomUUID());
        jdbc.update("INSERT INTO shop(shop_id, name, siret_number, status, address_id, category_id) " +
                "VALUES (?, ?, ?, 'VALIDATED', ?, ?)", shopBakery, "Chez Paul", "11111111111111", addrBakery, catBakery);
        jdbc.update("INSERT INTO shop(shop_id, name, siret_number, status, address_id, category_id) " +
                "VALUES (?, ?, ?, 'VALIDATED', ?, ?)", shopPress, "Le Kiosque", "22222222222222", addrPress, catPress);
    }

    @AfterEach
    void tearDown() {
        jdbc.update("DELETE FROM shop WHERE shop_id IN (?, ?)", shopBakery, shopPress);
        jdbc.update("DELETE FROM address WHERE address_id IN (?, ?)", addrBakery, addrPress);
        jdbc.update("DELETE FROM category WHERE category_id IN (?, ?)", catBakery, catPress);
    }

    @Test
    void returnsAllShopsWithoutFilter() {
        given().when().get("/shopkeepers").then().statusCode(200)
                .body("name", hasItems("Chez Paul", "Le Kiosque"));
    }

    @Test
    void filtersByCategoryAndReturnsReadableAddress() {
        given().queryParam("category", "TestBoulangerie").when().get("/shopkeepers")
                .then().statusCode(200).body("$", hasSize(1))
                .body("[0].name", equalTo("Chez Paul"))
                .body("[0].category", equalTo("TestBoulangerie"))
                .body("[0].address", equalTo("3 rue Nationale"));
    }

    @Test
    void filtersWithoutCaseSensitivity() {
        given().queryParam("category", "testboulangerie").when().get("/shopkeepers")
                .then().statusCode(200).body("$", hasSize(1));
    }

    @Test
    void returnsEmptyListForUnknownCategory() {
        given().queryParam("category", "CategorieInexistante").when().get("/shopkeepers")
                .then().statusCode(200).body("$", empty());
    }
}
