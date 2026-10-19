package com.glop.catalogue;

import com.glop.catalogue.in.restapi.payload.UpdateTestMessage;
import com.glop.catalogue.out.postgresdb.entity.TestEntity;
import com.glop.catalogue.out.postgresdb.repository.TestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
public class TestContainerControllerIntegration {

    @Autowired
    RestTestClient client;

    @Autowired
    TestRepository testRepository;

    @Test
    void postThenGet_persistsMessageInRealPostgresContainer() {
        UpdateTestMessage body = new UpdateTestMessage();
        body.setMessage("hello testcontainers");

        client.post().uri("/test")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.testMessage1").isEqualTo("hello testcontainers");

        client.get().uri("/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.testMessage1").isEqualTo("hello testcontainers");

        Optional<TestEntity> stored = testRepository.findFirstByOrderByTestIdAsc();
        assertTrue(stored.isPresent());
        assertEquals("hello testcontainers", stored.get().getMessage());
    }
}
