package com.reactivespring.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class FluxAndMonoCombineTest {

    @Test
    void combineUsingMerge() {
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("A", "B", "C");

        Flux<String> mergedFlux = Flux.merge(flux1, flux2);

        StepVerifier.create(mergedFlux.log())
                .expectNext("A", "B", "C", "A", "B", "C")
                .verifyComplete();
    }

    @Test
    void combineUsingMerge_WithDelay() {
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.merge(flux1, flux2);

        StepVerifier.create(mergedFlux.log())
//                .expectNext("A", "B", "C", "D", "E", "F")
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void combineUsingMerge_WithConcat() {
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> mergedFlux = Flux.concat(flux1, flux2);

        StepVerifier.create(mergedFlux.log())
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void combineUsingMerge_WithDelayAndConcat() {
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.concat(flux1, flux2);

        StepVerifier.create(mergedFlux.log())
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void combineUsingMerge_WithDelayAndConcat_improvedVirtualTime() {
        VirtualTimeScheduler.getOrSet();
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.concat(flux1, flux2);

        StepVerifier.withVirtualTime(() -> mergedFlux.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(6))
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void combineUsingMerge_WithZip() {
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.zip(flux1, flux2, String::concat);

        StepVerifier.create(mergedFlux.log())
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }
}
