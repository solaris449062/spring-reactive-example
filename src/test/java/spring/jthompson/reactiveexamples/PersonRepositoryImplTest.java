package spring.jthompson.reactiveexamples;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.jthompson.reactiveexamples.domain.Person;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {

    PersonRepositoryImpl personRepository;
    @BeforeEach
    void setUp() {
        personRepository = new PersonRepositoryImpl();
    }

    @Test
    void getByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);

        Person person = personMono.block();

        System.out.println(person.toString());
    }

    @Test
    void getByIdSubscribe() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    // running below test does not print anything, because there is no backpressure triggering the action. We need subscription.
    @Test
    void getByIdMapFunctionWithoutSubscription() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.map(person -> {
            System.out.println(person.toString());
            return person;
        });
    }

    @Test
    void getByIdMapFunctionWithSubscription() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.map(person -> {
            System.out.println(person.toString());
            return person.getFirstName();
        }).subscribe(firstName -> {
            System.out.println("from map: " + firstName);
        });
    }

    @Test
    void fluxTest() {
        Flux<Person> personFlux = personRepository.findAll();
        Person person = personFlux.blockFirst();
        System.out.println(person.toString());
    }

    @Test
    void testFluxSubscribe() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFluxToListMono() {
        Flux<Person> personFlux  = personRepository.findAll();

        Mono<List<Person>> personListMono = personFlux.collectList();

        personListMono.subscribe(list -> {
            list.forEach(person -> {
                System.out.println(person.toString());
            });
        });
    }


    @Test
    void testFindPersonById() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer id = 3;

        // this filters the values that matches the predicate, and emits a new Mono with the value by .next()
        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).next();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    // this test will not produce any console output
    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer id = 8; // this is nonexistent id

        // this filters the values that matches the predicate, and emits a new Mono with the value by .next()
        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).next();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFindPersonByIdNotFoundWithException() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer id = 8; // this is nonexistent id

        // this filters the values that matches the predicate, and expects a single Mono. If non found, then
        // issue an exception.
        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).single();

        personMono.doOnError(throwable -> {
            System.out.println("Custom error message: " + throwable.getMessage());
        }).onErrorReturn(Person.builder().id(id).build()).subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testGetPersonById() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.subscribe(person -> System.out.println(person.toString()));
    }
}