package com.reactivespring.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

public class FluxAndMonoErrorTest {

    @Test
    void fluxErrorHandling() {
        Flux<String> just = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error.")))
                .concatWith(Flux.just("D"));
        StepVerifier.create(just.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void fluxErrorHandling2_OnErrorResume() {
        Flux<String> just = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error.")))
                .concatWith(Flux.just("D"))
                .onErrorResume((e) -> {
                    System.out.println("Print Exception is " + e);
                    return Flux.just("Default value");
                });
        StepVerifier.create(just.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("Default value")
                .verifyComplete();
    }

    @Test
    void fluxErrorHandling_OnErrorReturn() {
        Flux<String> just = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error.")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("Default value");
        StepVerifier.create(just.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("Default value")
                .verifyComplete();
    }

    @Test
    void fluxErrorHandling_OnErrorMap() {
        Flux<String> just = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error.")))
                .concatWith(Flux.just("D"))
                .onErrorMap(CustomException::new);

        StepVerifier.create(just.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    void fluxErrorHandling_OnErrorMap_WithRetry() {
        Flux<String> just = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error.")))
                .concatWith(Flux.just("D"))
                .onErrorMap(CustomException::new)
                .retry(1);

        StepVerifier.create(just.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    void fluxErrorHandling_OnErrorMap_WithRetryBackoff() {
        Flux<String> just = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error.")))
                .concatWith(Flux.just("D"))
                .onErrorMap(CustomException::new)
                .retryWhen(Retry.backoff(1, Duration.ofSeconds(5)));

        StepVerifier.create(just.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException.class)
                .verify();
    }
}
