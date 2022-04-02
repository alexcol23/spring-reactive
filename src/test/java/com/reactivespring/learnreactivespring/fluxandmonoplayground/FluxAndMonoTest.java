package com.reactivespring.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    void fluxTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactive Spring")
//                .concatWith(Flux.error(new RuntimeException("Error message")))
                .concatWith(Flux.just("After Error"))
                .log();
        stringFlux.subscribe(System.out::println,
                (e) -> System.err.println("Exception is " + e),
                () -> System.out.println("Completed."));
    }

    @Test
    void fluxTestElements_WithOutError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactive Spring")
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring boot")
                .expectNext("Reactive Spring")
                .verifyComplete();
    }

    @Test
    void fluxTestElements_WithError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Error message")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring boot")
                .expectNext("Reactive Spring")
//                .expectError(RuntimeException.class)
                .expectErrorMessage("Error message")
                .verify();
//                .verifyError(RuntimeException.class);
    }

    @Test
    void fluxTestElementsCount_WithError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Error message")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .expectErrorMessage("Error message")
                .verify();
    }

    @Test
    void fluxTestElementsCount_WithError1() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Error message")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .expectErrorMessage("Error message")
                .verify();
    }

    @Test
    void monoTest_WithOutError() {
        Mono<String> stringMono = Mono.just("Spring");
        StepVerifier.create(stringMono.log())
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    void monoTest_WithError() {

        StepVerifier.create(Mono.error(new RuntimeException("Error message mono.")).log())
                .expectError(RuntimeException.class)
                .verify();
    }
}
