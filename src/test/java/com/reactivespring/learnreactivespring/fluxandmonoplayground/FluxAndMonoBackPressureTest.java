package com.reactivespring.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest {

    @Test
    void backPressureTest() {
        Flux<Integer> finiteFlux = Flux.range(1, 10)
                .log();
        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    void backPressure() {
        Flux<Integer> finiteFlux = Flux.range(1, 10)
                .log();
        finiteFlux.subscribe(
                System.out::println,
                (e) -> System.out.println("Error is " + e),
                () -> System.out.println("Done"),
                (subscription -> subscription.request(2)));
    }

    @Test
    void backPressure_WithCancel() {
        Flux<Integer> finiteFlux = Flux.range(1, 10)
                .log();
        finiteFlux.subscribe(
                System.out::println,
                (e) -> System.out.println("Error is " + e),
                () -> System.out.println("Done"),
                (subscription -> subscription.cancel()));
    }

    @Test
    void CustomizedBackPressure() {
        Flux<Integer> finiteFlux = Flux.range(1, 10)
                .log();
        finiteFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                System.out.println("Value received is : " + value);
                if (value == 4) {
                    cancel();
                }
            }
        });
    }
}
