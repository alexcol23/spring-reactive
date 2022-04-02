package com.reactivespring.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {
    List<String> names = Arrays.asList("adam", "anna", "mapis");

    @Test
    void FilterTest_WithOutErrors() {
        Flux<String> namesFlux = Flux.fromIterable(names)
                .filter(s -> s.startsWith("a"))
                .log();
        StepVerifier.create(namesFlux)
                .expectNext("adam", "anna")
                .verifyComplete();
    }

    @Test
    void FilterTestLength_WithOutErrors() {
        Flux<String> namesFlux = Flux.fromIterable(names)
                .filter(s -> s.length() > 4)
                .log();
        StepVerifier.create(namesFlux)
                .expectNext("mapis")
                .verifyComplete();
    }
}
