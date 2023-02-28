package spring.jthompson.reactiveexamples;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.jthompson.reactiveexamples.domain.Person;

public interface PersonRepository {

    Mono<Person> getById(Integer id);
    Flux<Person> findAll();
}
