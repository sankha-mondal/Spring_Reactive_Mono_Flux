package com;

import java.time.Duration;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
class SpringReactiveMonoFluxApplicationTests {

	// ================================================================================================================

	// ====== : Theory | Working | Example : =======

	@Test
	void testMono() {
		Mono<String> mono = Mono.just("Mackbook Pro");
		mono.log().map(data -> data.toUpperCase()).subscribe(data -> System.out.println(data));
	}

	// *************** OR *****************

	@Test
	void test_Mono() {
		Mono<String> mono = Mono.just("Mackbook Pro");
		mono.log().map(data -> data.toUpperCase()).subscribe(System.out::println);
	}

//=====================================================================================================================

	@Test
	void testFulx() {
		Flux<String> flux = Flux.just("Mackbook Pro", "IPhone", "Dell");
		flux.log().map(data -> data.toUpperCase()).subscribe(data -> System.out.println(data));
	}

	// *************** OR *****************
	// Use more useful methods | delayElements(), fromIterable

	@Test
	void test_Fulx_01() throws InterruptedException {
		Flux.just("Mackbook Pro", "IPhone", "Dell").delayElements(Duration.ofSeconds(2)).log()
				.map(data -> data.toUpperCase()).subscribe(new OrderConsumer());

		Thread.sleep(6000); // because our test execution will not wait for 2 sec.It's called Unblocking
							// nature
	}

	@Test
	void test_Fulx_02() throws InterruptedException {
		Flux.fromIterable(Arrays.asList("Mackbook Pro", "IPhone", "Dell")).delayElements(Duration.ofSeconds(2)).log()
				.map(data -> data.toUpperCase()).subscribe(new OrderConsumer());

		Thread.sleep(6000); // because our test execution will not wait for 2 sec.It's called Unblocking nature
	}

	@Test
	void test_Fulx_03() throws InterruptedException {
		Flux.fromIterable(
				Arrays.asList("Mackbook Pro", "IPhone", "Dell", "Book", "Phone", "HP", "Pro", "Mobile", "Box"))
				.delayElements(Duration.ofSeconds(2)).log().map(data -> data.toUpperCase())
				.subscribe(new Subscriber<String>() {

					private long count = 0;
					private Subscription subscription;

					@Override
					public void onSubscribe(Subscription subscription) {
						this.subscription = subscription;
						subscription.request(3); // no of elements in one batch | How many requests Subscribe ready to handle
						// subscription.request(Long.MAX_VALUE);
					}

					@Override
					public void onNext(String data) {
						count++;
						if (count >= 3) {
							count = 0;
							subscription.request(3);
						}
						System.out.println(data);
					}

					@Override
					public void onError(Throwable t) {
						t.printStackTrace();
					}

					@Override
					public void onComplete() {
						System.out.println("Completely Done...");
					}
				});

		Thread.sleep(20000); // because our test execution will not wait for 2 sec.It's called Unblocking nature
	}

}
