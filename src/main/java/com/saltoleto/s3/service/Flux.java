import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create a list of strings
        List<String> stringList = Arrays.asList("Hello", "World", "Flux", "Example");

        // Create a Flux from the list
        Flux<String> stringFlux = Flux.fromIterable(stringList);

        // Use flatMap to transform and flatten the elements
        stringFlux
            .flatMap(s -> Flux.just(s.toUpperCase()).subscribeOn(Schedulers.parallel()))
            .subscribe(System.out::println);
    }
}
