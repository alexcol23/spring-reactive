package com.reactivespring.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {
    @Test
    void infiniteSequence() throws InterruptedException {
        Flux<Long> interval = Flux.interval(Duration.ofMillis(200))
                .log();
        interval.subscribe(System.out::println);
        Thread.sleep(3000);
    }

    @Test
    void infiniteSequenceTest() {
        Flux<Long> interval = Flux.interval(Duration.ofMillis(200))
                .take(3)
                .log();
        StepVerifier.create(interval)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }

    @Test
    void infiniteSequenceTest_WithMap() throws InterruptedException {
        Flux<Integer> interval = Flux.interval(Duration.ofMillis(200))
                .map(Long::intValue)
                .take(3)
                .log();
        StepVerifier.create(interval)
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();
    }

    @Test
    void infiniteSequenceTest_WithMapAndDelay() throws InterruptedException {
        Flux<Integer> interval = Flux.interval(Duration.ofMillis(200))
                .delayElements(Duration.ofSeconds(1))
                .map(Long::intValue)
                .take(3)
                .log();
        StepVerifier.create(interval)
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();
    }
}
