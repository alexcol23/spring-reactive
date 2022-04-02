package com.reactivespring.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdAndHotPublisherTest {
    @Test
    void coldPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1));
        stringFlux.subscribe(s -> System.out.println("Sub 1 = " + s));
        Thread.sleep(2000);
        stringFlux.subscribe(s -> System.out.println("Sub 2 = " + s));
        Thread.sleep(4000);
    }

    @Test
    void hotPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1));
        ConnectableFlux<String> publish = stringFlux.publish();
        publish.connect();
        publish.subscribe(s -> System.out.println("Sub 1 = " + s));
        Thread.sleep(3000);
        publish.subscribe(s -> System.out.println("Sub 2 = " + s));
        Thread.sleep(4000);
    }
}
