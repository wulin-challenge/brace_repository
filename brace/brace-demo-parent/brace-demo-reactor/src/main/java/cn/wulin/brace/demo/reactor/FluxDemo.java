package cn.wulin.brace.demo.reactor;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

public class FluxDemo {
	
	@Test
	public void fluxTest() {
		Flux.just("Hello", "World").subscribe(System.out::println);
		Flux.fromArray(new Integer[] {1, 2, 3}).subscribe(System.out::println);
		Flux.empty().subscribe(System.out::println);
		Flux.range(1, 10).subscribe(System.out::println);
		Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).subscribe(System.out::println);
//		Flux.inintervalMillis(1000).subscribe(System.out::println);
	}
	
	@Test
	public void bufferTest() {
		long start = System.currentTimeMillis();
//		Flux.range(1, 100).buffer(20).subscribe(System.out::println);
		Flux.range(1, 100).subscribe(System.out::println);
		long end = System.currentTimeMillis();
		System.out.println((end-start));
		
		System.out.println("-----------------------------");
		System.out.println("-----------------------------");
		System.out.println("-----------------------------");
		
		long start1 = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			System.out.println(i);
		}
		long end1 = System.currentTimeMillis();
		System.out.println((end1-start1));
		
	}
	
	@Test
	public void FilterTest() {
		Flux.range(1, 10).filter(i -> i % 2 == 0).subscribe(System.out::println);
	}
	
	@Test
	public void takeTest() {
		List<Integer> list= new ArrayList<>();
		Flux.range(1, 1000).take(10).subscribe(e->list.add(e));
		
		System.out.println(list);
		
		Flux.range(-10, 1000).takeUntil(i -> i == 7).subscribe(System.out::println);
	}
	
	@Test
	public void reduceTest() {
		Flux.range(1, 10).reduce((x, y) -> {
			System.out.println(x+":"+y);
			return x*y;
		}).subscribe(System.out::println);
	}
	
	@Test
	public void reduceWithTest() {
		Flux.range(1, 100).reduceWith(() -> 10, (x, y) -> x + y).subscribe(System.out::println);
	}

	
	@Test
	public void mergeTest() {
		Flux.merge(Flux.interval(Duration.ofMillis(100)).take(5), Flux.interval(Duration.ofMillis(200)).take(5))
		.toStream()
		.forEach(System.out::println);
		
		
	}
	
	@Test
	public void exceptionTest() {
		//调试代码
//		Hooks.onOperatorDebug();
		Hooks.onOperatorError((x,y)->{
			x.printStackTrace();
			return x;
		});
		Flux.range(1, 100).reduce((x, y) -> {
			System.out.println(x+":"+y);
			int i = 1/0;
			return x*y;
		}).subscribe(System.out::println);
	}
	
	
}
