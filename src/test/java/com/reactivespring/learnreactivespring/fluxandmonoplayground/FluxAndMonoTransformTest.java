package com.reactivespring.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {
    List<String> names = Arrays.asList("adam", "anna", "mapis");

    @Test
    void transformUsingMap_WithOutErrors() {
        Flux<String> namesFlux = Flux.fromIterable(names)
                .map(String::toUpperCase)
                .log();
        StepVerifier.create(namesFlux)
                .expectNext("ADAM", "ANNA", "MAPIS")
                .verifyComplete();
    }

    @Test
    void transformUsingMapLength_WithOutErrors() {
        Flux<Integer> namesFlux = Flux.fromIterable(names)
                .map(String::length)
                .log();
        StepVerifier.create(namesFlux)
                .expectNext(4, 4, 5)
                .verifyComplete();
    }

    @Test
    void transformUsingMapLengthRepeat_WithOutErrors() {
        Flux<Integer> namesFlux = Flux.fromIterable(names)
                .map(String::length)
                .repeat(1)
                .log();
        StepVerifier.create(namesFlux)
                .expectNext(4, 4, 5, 4, 4, 5)
                .verifyComplete();
    }

    @Test
    void transformUsingMapFilter_WithOutErrors() {
        Flux<String> namesFlux = Flux.fromIterable(names)
                .filter(s -> s.length() > 4)
                .map(String::toUpperCase)
                .repeat(1)
                .log();
        StepVerifier.create(namesFlux)
                .expectNext("MAPIS", "MAPIS")
                .verifyComplete();
    }

    @Test
    void transformUsingFlatMap_WithOutErrors() {
        Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .flatMap(s -> Flux.fromIterable(convertToList(s))).log();
        StepVerifier.create(namesFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "newValue");
    }

    @Test
    void transformUsingFlatMap_UsingParallel_WithOutErrors() {
        Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2)
                .flatMap((s) ->
                        s.map(this::convertToList).subscribeOn(parallel()))
                .flatMap(Flux::fromIterable)
                .log();
        StepVerifier.create(namesFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    void transformUsingFlatMap_UsingParallelOrder_WithOutErrors() {
        Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2)
                .concatMap((s) ->
                        s.map(this::convertToList).subscribeOn(parallel()))
                .flatMap(Flux::fromIterable)
                .log();
        StepVerifier.create(namesFlux)
                .expectNextCount(12)
                .verifyComplete();
    }
}
