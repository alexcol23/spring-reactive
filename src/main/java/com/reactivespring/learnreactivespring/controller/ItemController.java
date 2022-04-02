package com.reactivespring.learnreactivespring.controller;

import com.reactivespring.learnreactivespring.constants.ItemConstants;
import com.reactivespring.learnreactivespring.document.Item;
import com.reactivespring.learnreactivespring.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    ItemReactiveRepository repository;

    @GetMapping(ItemConstants.ITEM_END_POINT_V1)
    public Flux<Item> getAllItems() {
        return repository.findAll();
    }

    @GetMapping(ItemConstants.ITEM_END_POINT_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> getOneItem(@PathVariable String id) {
        return repository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(ItemConstants.ITEM_END_POINT_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item) {
        return repository.save(item);
    }

    @DeleteMapping(ItemConstants.ITEM_END_POINT_V1 + "/{id}")
    public Mono<Void> deleteItem(@PathVariable String id) {
        return repository.deleteById(id);
    }

    @PutMapping(ItemConstants.ITEM_END_POINT_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id, @RequestBody Item newItem) {
        return repository.findById(id)
                .flatMap(item -> {
                    item.setDescription(newItem.getDescription());
                    item.setPrice(newItem.getPrice());
                    return repository.save(item);
                })
                .map(updateItem -> new ResponseEntity<>(updateItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
