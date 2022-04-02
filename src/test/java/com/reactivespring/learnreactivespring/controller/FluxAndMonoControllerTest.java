package com.reactivespring.learnreactivespring.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void flux_approach1() {
        Flux<Integer> integerFlux = webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();
        StepVerifier.create(integerFlux)
                .expectSubscription()
                .expectNext(0, 1, 2, 3)
                .verifyComplete();
    }

    @Test
    void flux_approach2() {
        webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .hasSize(4);
    }

    @Test
    void flux_approach3() {
        List<Integer> expectedList = Arrays.asList(0, 1, 2, 3);
        EntityExchangeResult<List<Integer>> listEntityExchangeResult = webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();
        Assertions.assertEquals(expectedList, listEntityExchangeResult.getResponseBody());
    }

    @Test
    void flux_approach4() {
        List<Integer> expectedList = Arrays.asList(0, 1, 2, 3);
        webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith(response -> {
                    Assertions.assertEquals(expectedList, response.getResponseBody());
                });
    }

    @Test
    void fluxStream() {
        Flux<Long> longStreamFlux = webTestClient.get()
                .uri("/fluxstream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(longStreamFlux)
                .expectSubscription()
                .expectNext(0l, 1l, 2l)
                .thenCancel()
                .verify();
    }

    @Test
    void MonoControllerTest() {
        webTestClient
                .get()
                .uri("/mono")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith(res -> {
                    Assertions.assertEquals(1, res.getResponseBody());
                });
    }

}