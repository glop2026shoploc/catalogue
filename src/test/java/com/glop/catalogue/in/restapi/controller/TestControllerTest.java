package com.glop.catalogue.in.restapi.controller;

import com.glop.catalogue.TestcontainersConfiguration;
import io.jsonwebtoken.Jwts;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class TestControllerTest {

    @LocalServerPort
    int port;

    /** Même clé que celle utilisée par l'Authenticator pour vérifier les tokens. */
    @Autowired
    private SecretKey jwtKey;

    private String bearerToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/catalogue";
        bearerToken = "Bearer " + clientToken();
    }

    @Test
    void shouldSaveMessageAndReturnItOnGet() {
        given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .body("{\"message\": \"Bonjour\"}")
        .when()
                .post("/test")
        .then()
                .statusCode(200)
                .body("testMessage1", equalTo("Bonjour"));

        given()
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
        .when()
                .get("/test")
        .then()
                .statusCode(200)
                .body("testMessage1", equalTo("Bonjour"))
                .body("testMessage2", equalTo("Test Message"));
    }

    /** Token CLIENT signé, au format attendu par AuthenticatedUser.from (UUID en String). */
    private String clientToken() {
        return Jwts.builder()
                .subject("client@test.fr")
                .claim("userId", UUID.randomUUID().toString())
                .claim("cityId", UUID.randomUUID().toString())
                .claim("role", "CLIENT")
                .claim("clientId", UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(Duration.ofMinutes(5))))
                .signWith(jwtKey)
                .compact();
    }
}
