package com.glop.catalogue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class TestControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/catalogue";
    }

    @Test
    void shouldSaveMessageAndReturnItOnGet() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"message\": \"Bonjour\"}")
        .when()
                .post("/test")
        .then()
                .statusCode(200)
                .body("testMessage1", equalTo("Bonjour"));

        given()
        .when()
                .get("/test")
        .then()
                .statusCode(200)
                .body("testMessage1", equalTo("Bonjour"))
                .body("testMessage2", equalTo("Test Message"));
    }
}
