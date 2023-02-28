package spring.jthompson.reactiveexamples;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.jthompson.reactiveexamples.domain.Person;

public class PersonRepositoryImpl implements PersonRepository {

    // emulating db-stored Person items
    Person john = new Person(1, "John", "Jackson");
    Person david = new Person(2, "David", "Griffiths");
    Person mary = new Person(3, "Mary", "Boas");

    Person jun = new Person(4, "Jun", "Sakurai");


    // Mono is used for at most 1 (0 or 1)
    @Override
    public Mono<Person> getById(Integer id) {
        return Mono.just(john);
    }

    // Flux is used for one or more
    @Override
    public Flux<Person> findAll() {
        return Flux.just(john, david, mary, jun);
    }
}
