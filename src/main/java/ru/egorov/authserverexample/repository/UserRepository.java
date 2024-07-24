package ru.egorov.authserverexample.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.egorov.authserverexample.model.User;

public interface UserRepository extends ReactiveCrudRepository<User, String> {

    Mono<User> findUsersByLogin(String login);
}
