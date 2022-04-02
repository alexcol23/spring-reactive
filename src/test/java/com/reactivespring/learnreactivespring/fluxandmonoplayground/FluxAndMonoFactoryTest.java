package com.reactivespring.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("adam", "anna", "mapis");

    @Test
    void fluxUsingIterable_WithOutErrors() {
        Flux<String> namesFlux = Flux.fromIterable(names)
                .log();
        StepVerifier.create(namesFlux)
                .expectNext("adam", "anna", "mapis")
                .verifyComplete();
    }

    @Test
    void fluxUsingArray_WithOutErrors() {
        String[] names = new String[]{"adam", "anna", "mapis"};

        Flux<String> namesFlux = Flux.fromArray(names)
                .log();
        StepVerifier.create(namesFlux)
                .expectNext("adam", "anna", "mapis")
                .verifyComplete();
    }

    @Test
    void fluxUsingStream_WithOutErrors() {

        Flux<String> namesFlux = Flux.fromStream(this.names.stream())
                .log();
        StepVerifier.create(namesFlux)
                .expectNext("adam", "anna", "mapis")
                .verifyComplete();
    }

    @Test
    void monoUsingJustOrEmpty_WithOutErrors() {
        Mono<Object> monoEmpty = Mono.justOrEmpty(null);
        StepVerifier.create(monoEmpty.log())
                .verifyComplete();
    }

    @Test
    void monoUsingSupplier_WithOutErrors() {
        Supplier<String> stringSupplier = () -> "return";
        Mono<String> stringMono = Mono.fromSupplier(stringSupplier);
        StepVerifier.create(stringMono.log())
                .expectNext("return")
                .verifyComplete();
    }

    @Test
    void fluxUsingRange_WithOutErrors() {
        Flux<Integer> integerFlux = Flux.range(1, 5);
        StepVerifier.create(integerFlux.log())
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }


}
