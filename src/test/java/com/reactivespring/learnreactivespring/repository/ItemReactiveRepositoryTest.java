package com.reactivespring.learnreactivespring.repository;

import com.reactivespring.learnreactivespring.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
//@ExtendWith(SpringExtension.class)
@DirtiesContext
public class ItemReactiveRepositoryTest {


    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> listItem = Arrays.asList(new Item(null, "Samsung TV", 200.0),
            new Item(null, "LG TV", 300.0),
            new Item(null, "Apple Watch", 400.0),
            new Item(null, "Beats Headphones", 500.0),
            new Item("ABC", "Macbook", 1500.0)
    );

    @BeforeEach
    public void setUp() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(listItem))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("inserted Item is = " + item))
                .blockLast();
    }

    @Test
    void getAllItemsTest() {
        Flux<Item> itemFlux = itemReactiveRepository.findAll();

        StepVerifier.create(itemFlux)
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    void getItemByTest() {
        Mono<Item> itemMono = itemReactiveRepository.findById("ABC");

        StepVerifier.create(itemMono)
                .expectSubscription()
                .expectNextMatches(item -> "Macbook".equals(item.getDescription()))
                .verifyComplete();
    }

    @Test
    void findItemByDescriptionTest() {
        Flux<Item> itemFlux = itemReactiveRepository.findByDescription("Macbook").log();

        StepVerifier.create(itemFlux)
                .expectSubscription()
                .expectNextMatches(item -> "ABC".equals(item.getId()))
                .verifyComplete();
    }

    @Test
    void saveItemTest() {

        Item item1 = new Item("DEF", "Google Home", 1500.0);

        Mono<Item> itemSaved = itemReactiveRepository.save(item1);

        StepVerifier.create(itemSaved)
                .expectSubscription()
                .expectNextMatches(item -> "DEF".equals(item.getId()))
                .verifyComplete();
    }

    @Test
    void updateItemTest() {

        Item item1 = new Item("ABC", "MackBook Pro 16", 1700.0);

        Mono<Item> itemSaved = itemReactiveRepository.save(item1);

        StepVerifier.create(itemSaved)
                .expectSubscription()
                .expectNextMatches(item -> "ABC".equals(item.getId()) && item1.getDescription().equals(item.getDescription()))
                .verifyComplete();
    }


    @Test
    void deleteItemTest() {
        Mono<Void> mono = itemReactiveRepository.findById("ABC")
                .map(Item::getId)
                .flatMap(item -> itemReactiveRepository.deleteById(item));

        StepVerifier.create(mono.log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findById("ABC"))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }

}
