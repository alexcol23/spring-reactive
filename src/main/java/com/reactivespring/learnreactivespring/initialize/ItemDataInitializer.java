package com.reactivespring.learnreactivespring.initialize;

import com.reactivespring.learnreactivespring.document.Item;
import com.reactivespring.learnreactivespring.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
public class ItemDataInitializer implements CommandLineRunner {

    @Autowired
    ItemReactiveRepository repository;

    @Override
    public void run(String... args) throws Exception {

        initialDataSetUp();
    }

    public List<Item> data() {
        return Arrays.asList(new Item(null, "Samsung TV", 200.0),
                new Item(null, "LG TV", 300.0),
                new Item(null, "Apple Watch", 400.0),
                new Item(null, "Beats Headphones", 500.0),
                new Item("ABC", "Macbook", 1500.0)
        );
    }

    private void initialDataSetUp() {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(data())
                        .flatMap(repository::save)
                        .thenMany(repository.findAll()))
                .subscribe(item -> System.out.println("Inserted Item = " + item));
    }
}
