package com.reactivespring.learnreactivespring.controller;

import com.reactivespring.learnreactivespring.constants.ItemConstants;
import com.reactivespring.learnreactivespring.document.Item;
import com.reactivespring.learnreactivespring.repository.ItemReactiveRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository repository;

    public List<Item> data() {
        return Arrays.asList(new Item(null, "Samsung TV", 200.0),
                new Item(null, "LG TV", 300.0),
                new Item(null, "Apple Watch", 400.0),
                new Item(null, "Beats Headphones", 500.0),
                new Item("ABC", "Macbook", 1500.0)
        );
    }

    @BeforeEach
    public void setUp() {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(repository::save)
                .doOnNext(item -> {
                    System.out.println("Inserted Item is = " + item);
                })
                .blockLast();
    }

    @Test
    void getAllItemsTest() {
        webTestClient.get()
                .uri(ItemConstants.ITEM_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(5);
    }

    @Test
    void getAllItemsTest_approach2() {
        webTestClient.get()
                .uri(ItemConstants.ITEM_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(5)
                .consumeWith(response -> {
                    response.getResponseBody().forEach(item -> Assertions.assertNotNull(item.getId()));
                });
    }

    @Test
    void getAllItemsTest_approach3() {
        Flux<Item> itemFlux = webTestClient.get()
                .uri(ItemConstants.ITEM_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Item.class)
                .getResponseBody();

        StepVerifier.create(itemFlux.log())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }


    @Test
    void getOneItemTest() {
        webTestClient.get()
                .uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"), "ABC")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 1500.0);
    }

    @Test
    void getOneItem_notFoundTest() {
        webTestClient.get()
                .uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"), "ABCE")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createItemTest() {
        Item item = new Item(null, "Iphone X", 550.0);
        webTestClient.post()
                .uri(ItemConstants.ITEM_END_POINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Iphone X");
    }

    @Test
    void DeleteItemByIdTest() {
        webTestClient.delete()
                .uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"), "ABC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);

    }

    @Test
    void UpdateItemTest() {
        double newPrice = 2500.0;
        Item item = new Item(null, "Macbook", newPrice);

        webTestClient.put()
                .uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"), "ABC")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", newPrice);

    }

    @Test
    void UpdateItemTest_withInvalidId() {
        double newPrice = 2500.0;
        Item item = new Item(null, "Macbook", newPrice);

        webTestClient.put()
                .uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"), "DEF")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void getAllItemsTest_approach4() {
        webTestClient.get()
                .uri(ItemConstants.ITEM_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(5)
                .consumeWith(response -> {
                    List<Item> items = response.getResponseBody();
                    assert items != null;
                    items.forEach(item -> {
                        Assertions.assertNotNull(item.getDescription());
                    });
                });
    }

}
