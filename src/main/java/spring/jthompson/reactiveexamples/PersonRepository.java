package spring.jthompson.reactiveexamples;

import spring.jthompson.reactiveexamples.domain.Person;

public interface PersonRepository {

    Mono<Person> getById(Integer id);
    Flux<Person> findAll();
}
