package com.reactivespring.learnreactivespring.handler;

import com.reactivespring.learnreactivespring.constants.ItemConstants;
import com.reactivespring.learnreactivespring.document.Item;
import com.reactivespring.learnreactivespring.repository.ItemReactiveRepository;
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

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemsHandlerTest {


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
                .uri(ItemConstants.FUNCTIONAL_ITEM_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(5);
    }

    @Test
    void getOneItemTest() {
        webTestClient.get()
                .uri(ItemConstants.FUNCTIONAL_ITEM_END_POINT_V1.concat("/{id}"), "ABC")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 1500.0);
    }

    @Test
    void getOneItem_notFoundTest() {
        webTestClient.get()
                .uri(ItemConstants.FUNCTIONAL_ITEM_END_POINT_V1.concat("/{id}"), "ABCE")
                .exchange()
                .expectStatus().isNotFound();
    }

}
